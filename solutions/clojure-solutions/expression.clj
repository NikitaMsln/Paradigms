;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; Common part ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn my-div [& args]
    (if (== (count args) 1)
        (/ 1 (double (first args)))
        (reduce #(/ (double %1) (double %2)) args)))

(defn make-parser [cnst-ctor var-ctor ops]
  (fn [expr]
      (letfn [(parseToken [token]
           (cond
                  (number? token) (cnst-ctor token)
                  (list? token) (apply (ops (first token)) (mapv parseToken (rest token)))
                  (symbol? token) (var-ctor (str token))
                  ))] (parseToken (read-string expr)))))

(defn get-truth-name [s] (.toString (nth (char-array (clojure.string/lower-case s)) 0)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; HT10 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn constant [value] (fn [vals] value))
(defn variable [name] (fn [vals] {:pre [(and (map? vals) (contains? vals name))]} (vals name)))
(def make-op
  (letfn [(help [op cnt] (fn [& operands]
                           {:pre [(or (= cnt -1) (= (count operands) cnt))]}
                           (fn [vals] (apply op (mapv #(% vals) operands)))))
          (ans
           ([op] (help op -1))
           ([op cnt] (help op cnt)))]
  ans))
(def add (make-op +'))
(def subtract (make-op -'))
(def multiply (make-op *'))
(def divide (make-op my-div))
(def negate (make-op - 1))
(def exp (make-op #(Math/exp %) 1))
(def ln (make-op #(Math/log (Math/abs %)) 1))
(def arcTan (make-op #(Math/atan %) 1))
(def arcTan2 (make-op #(Math/atan2 %1 %2)))
(defn sumexp [& operands] (apply add (mapv exp operands)))
(def lse (comp ln sumexp))
(defn softmax [& operands] (divide (exp (first operands)) (apply sumexp operands)))
(def square (make-op #(* % %) 1))
(def sqrt (make-op #(Math/sqrt %) 1))
(defn meansq [& operands] (divide (apply add (mapv square operands)) (constant (count operands))))
(def rms (comp sqrt meansq))
(def get-op {'+ add '- subtract '* multiply '/ divide 'negate negate
             'exp exp 'ln ln 'sqrt sqrt 'square square
             'atan arcTan 'atan2 arcTan2
             'lse lse 'sumexp sumexp 'softmax softmax
             'meansq meansq 'rms rms})
(def parseFunction (make-parser constant variable get-op))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; HT11 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; TODO: remove `examples/`
;(load-file "examples/proto.clj")
(load-file "proto.clj")

(def evaluate (method :evaluate))
(def toString (method :toString))
(def toStringInfix (method :toStringInfix))
(def diff (method :diff))
(defn Expression [evaluate toString toStringInfix diff]
    {:evaluate evaluate :toString toString :toStringInfix toStringInfix :diff diff})

(def value (field :value))
(declare ZERO)
(def Constant
  (constructor
    (fn [this c] (assoc this :value c))
    (Expression
      (fn [this vars] (value this))
      (fn [this] (str (value this)))
      toString
      (fn [this diffName] ZERO))))
(def ZERO (Constant 0))
(def ONE (Constant 1))

(def _name (field :name))
(def _exact-name (field :exact-name))
(def Variable
  (constructor
    (fn [this v] (assoc this :name v :exact-name (get-truth-name v)))
    (Expression
      (fn [this vars] (get vars (_exact-name this)))
      (fn [this] (_name this))
      toString
      (fn [this diffName] (if (= (_exact-name this) diffName) ONE ZERO)))))

(def operator (field :operator))
(def function (field :function))
(def operands (field :operands))
(def diffOp (field :diffOp))
(def cntArgs (field :cntArgs))
(def Operation
    (constructor
     (fn [this function operator diffOp] (assoc this :function function :operator operator :diffOp diffOp))
     (Expression
      (fn [this vars] (apply (function this) (mapv #(evaluate % vars) (operands this))))
      (fn [this] (str "("
                      (operator this) " " (clojure.string/join " " (mapv #(toString %) (operands this)))
                      ")"))
      (fn [this] (if (== (cntArgs this) 1)
                     (str (operator this) "(" (toStringInfix (first (operands this))) ")")
                     (str "("
                          (clojure.string/join (str " " (operator this) " ")
                                               (mapv toStringInfix (operands this)))
                          ")")))
      (fn [this diffName] ((diffOp this) (mapv #(diff % diffName) (operands this)) (operands this))))))

(defn make-obj-op [function operator diffOp]
    (constructor
     (fn [this & args] (assoc this :operands (vec args) :cntArgs (count args)))
     (Operation function operator diffOp)))

; :NOTE: +'
; fixed
(def Add (make-obj-op + "+" (fn [f' _] (apply Add f'))))

(def Subtract (make-obj-op - "-" (fn [f' _] (apply Subtract f'))))

(def Negate (make-obj-op #(- %) "negate" (fn [[f'] _] (Negate f'))))

(declare Multiply Divide Sumexp)

; :NOTE: -reduce
; fixed
(defn diff-mul [f' f]
  (apply Add (mapv
               (fn [i] (apply Multiply (assoc f i (f' i))))
               (range (count f')))))

(def Multiply (make-obj-op * "*" diff-mul))

(defn diff-div [f' f]
    (let [a' (first f') a (first f)
          g' (subvec f' 1) g (subvec f 1)
          z (apply Multiply g)]
        (if (empty? g)
            (diff-div [ZERO a'] [ONE a])
            (Divide (Subtract (Multiply a' z) (Multiply a (diff-mul g' g))) (Multiply z z)))))

(def Divide (make-obj-op my-div "/" diff-div))

(defn diff-sumexp [f' f] (apply Add (mapv #(Multiply (Sumexp %2) %1) f' f)))

(def Sumexp
    (make-obj-op
     (fn [& args] (apply + (mapv #(Math/exp %) args)))
     "sumexp" diff-sumexp))

(def Softmax
    (make-obj-op
     (fn [& args] (/ (Math/exp (first args)) (apply + (mapv #(Math/exp %) args))))
     "softmax"
     (fn [f' f]
         (Multiply
          (apply Softmax f)
          (Subtract (first f') (Divide (diff-sumexp f' f) (apply Sumexp f)))))))

(defn bit-op [op] (fn [a b] (Double/longBitsToDouble (op (Double/doubleToLongBits a) (Double/doubleToLongBits b)))))

(def BitAnd (make-obj-op (bit-op bit-and) "&" (fn [_ _] nil)))

(def BitOr (make-obj-op (bit-op bit-or) "|" (fn [_ _] nil)))

(def BitXor (make-obj-op (bit-op bit-xor) "^" (fn [_ _] nil)))

(defn impl [a b] (bit-op #(bit-or (bit-not %1) %2)))

(def BitImpl (make-obj-op impl "=>" (fn [_ _] nil)))

(def BitIff (make-obj-op (fn [a b] ((bit-op bit-and) (impl a b) (impl b a))) "<=>" (fn [_ _] nil)))

; :NOTE: Форматирование
; fixed
(def get-obj-op {
        '+ Add
        '- Subtract
        '* Multiply
        '/ Divide
        'negate Negate
        'sumexp Sumexp
        'softmax Softmax
        '& BitAnd
        '| BitOr
        (symbol "^") BitXor
        '=> BitImpl
        '<=> BitIff
        })

(def parseObject (make-parser Constant Variable get-obj-op))

;(println (toString (Constant 12)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; HT12 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; TODO: remove `examples/`
;(load-file "examples/parser.clj")
;(load-file "parser.clj")
;
;(defn +take-string [s] (+str (apply +seq (mapv (comp +char str) (char-array s)))))
;
;(defn r? [chars] (+opt (+char chars)))
;(defn r+ [chars] (+plus (+char chars)))
;(defn r* [chars] (+star (+char chars)))
;
;(def *all-chars (mapv char (range 32 128)))
;(defn *filter [f] (+char (apply str (filter f *all-chars))))
;;(def *alpha (*filter #(Character/isLetter %)))
;(def *alpha (+char "xyzXYZ"))
;(def *uint (+plus (*filter #(Character/isDigit %))))
;(def *number (+map read-string (+str (+seqf #(flatten %&) (r? "-") *uint (r? ".") (+opt *uint)))))
;(def *ws (+ignore (+star (*filter #(Character/isWhitespace %)))))
;(defn *skip-ws [op] (+seqn 0 *ws op *ws))
;(def *open-br (+ignore (*skip-ws (+char "("))))
;(def *close-br (+ignore (*skip-ws (+char ")"))))
;(defn *with-br [op] (*skip-ws (+seqn 0 *open-br (*skip-ws op) *close-br)))
;
;(def *op #(+map (comp symbol str) (+str (+seqn 0 *ws (+take-string %) *ws))))
;
;(def *variable (+map Variable (+str (*skip-ws (+plus *alpha)))))
;(def *constant (+map Constant (*skip-ws *number)))
;
;(def *not-bin-op)
;(def *negate (+map Negate (+seqn 1 *ws (+take-string "negate") (delay *not-bin-op))))
;
;(defn *bin-op [ops next-lvl]
;    (+map #(reduce (fn [a b] ((get-obj-op (nth b 0)) a (nth b 1))) (nth % 0) (nth % 1))
;          (+seq next-lvl (+star (+seq ops next-lvl)))))
;
;(def *expression)
;(def *not-bin-op (+or *negate *constant *variable (*with-br (delay *expression))))
;(def *mul-div (*bin-op (+or (*op "*") (*op "/")) *not-bin-op))
;(def *add-sub (*bin-op (+or (*op "+") (*op "-")) *mul-div))
;(def *and (*bin-op (*op "&") *add-sub))
;(def *or (*bin-op (*op "|") *and))
;(def *xor (*bin-op (*op "^") *or))
;(def *impl (*bin-op (*op "=>") *xor))
;(def *iff (*bin-op (*op "<=>") *impl))
;(def *expression (*skip-ws *iff))
;
;(def parseObjectInfix (+parser *expression))
;
;(println (evaluate (parseObjectInfix "x<=>y") {"x" 1 "y" 5}))

