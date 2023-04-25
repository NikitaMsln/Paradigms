;; Templates
(defn type-len-eq [checker-coll checker-type & args]
    (let [checker (partial checker-coll checker-type)]
        (and (every? checker args) (apply == (map count args)))
        )
    )

(defn is-vector? [checker v] (and (vector? v) (every? checker v)))

(defn coord-func [checker op]
    (fn [& args]
        {:pre [(every? (partial checker (first args)) args)] :post [(checker (first args) %)]}
        (apply mapv op args)
        )
    )

(defn apply-op [op] (fn [& args] (reduce op args)))

;; Vectors
(def v-len-eq (partial type-len-eq is-vector? number?))
(def v-func (partial coord-func v-len-eq))

(def v+ (v-func +'))
(def v- (v-func -'))
(def v* (v-func *'))
(def vd (v-func /))

(defn scalar [& vs] {:post [(number? %)]} (reduce + (apply v* vs)))

(defn v*s [v & nums]
    {:pre [(and (is-vector? number? v) (every? number? nums))] :post [(v-len-eq v %)]}
    (let [num (reduce * nums)]
        (mapv (partial * num) v)
        )
    )

;; Matrices
(defn is-matrix? [m] (is-vector? #(and (vector? %) (partial v-len-eq %)) m))
(defn m-len-eq [m-1 m-2]
    (and
     (is-matrix? m-1)
     (is-matrix? m-2)
     (type-len-eq every? vector? m-1 m-2)
     (v-len-eq (first m-1) (first m-2))
     )
    )

(def m-func (partial coord-func m-len-eq))

(def m+ (m-func v+))
(def m- (m-func v-))
(def m* (m-func v*))
(def md (m-func vd))

(defn m*s [m & nums]
    {:pre [(and (is-matrix? m) (every? number? nums))] :post [(m-len-eq m %)]}
    (let [num (reduce * nums)]
        (mapv #(v*s % num) m)
        )
    )

(defn m*v [m v]
    {:pre [(and
            (is-matrix? m)
            (is-vector? number? v)
            (== (count v) (count (first m)))
            )]
     :post [(and (is-vector? number? %) (== (count %) (count m)))]}
    (mapv (partial scalar v) m)
    )

(defn compatible? [x y] {:pre [(and (is-matrix? x) (is-matrix? y))]}
    (== (count y) (count (first x)))
    )

(defn transpose [m]
    {:pre [(is-matrix? m)]
     :post [(or (== (count %) 0) (and (compatible? m %) (compatible? % m)))]}
    (apply mapv vector m)
    )

(def m*m
    (apply-op
     (fn [m-1 m-2]
         {:pre [(compatible? m-1 m-2)]
          :post [(and
                  (== (count %) (count m-1))
                  (== (count (first %)) (count (first m-2)))
                  )]
          }
         (mapv (partial m*v (transpose m-2)) m-1)
         )
     )
    )

;; Vectors -- vect
(def vect
    (apply-op
     (fn [v-1 v-2] {:pre [(and (v-len-eq v-1 v-2) (== (count v-1) 3))] :post [(v-len-eq v-1 %)]}
         (let [[x1 y1 z1] v-1 [x2 y2 z2] v-2]
             [(- (* y1 z2) (* y2 z1))
              (- (* z1 x2) (* z2 x1))
              (- (* x1 y2) (* x2 y1))]
             )
         )
     )
    )

;; Tensors
(defn t-len-eq [a b]
    (or (and (number? a) (number? b)) (v-len-eq a b)
        (and
         (type-len-eq is-vector? vector? a b)
         (every? true? (mapv t-len-eq a b))
         )
        )
    )

; is y suffix of x?
(defn is-suffix-of? [x y]
    (or
     (t-len-eq x y)
     (and (vector? x) (is-suffix-of? (first x) y))
     )
    )

; broadcast y to x
(defn broadcast [x y] {:pre  [(is-suffix-of? x y)] :post [(t-len-eq % x)]}
    (if (t-len-eq x y) y (mapv #(broadcast % y) x))
    )

(defn apply-op-t [e fun]
    (fn [& args] (if (== (count args) 1) (fun e (first args)) (reduce fun args)))
    )
(defn hb-func [op e]
    (apply-op-t e
                (fn [x y]
                    (letfn [(fun [& args]
                                 (if (every? number? args)
                                     (apply op args)
                                     (apply mapv fun args)
                                     )
                                 )]
                        (fun (if (is-suffix-of? x y) x (broadcast y x))
                             (if (is-suffix-of? x y) (broadcast x y) y)
                             )
                        )
                    )
                )
    )

(def hb+ (hb-func +' 0))
(def hb- (hb-func -' 0))
(def hb* (hb-func *' 1))
(def hbd (hb-func / 1))
