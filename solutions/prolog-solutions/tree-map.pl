node(K, V, tree(K, V, Y, null, null, 1)) :- rand_int(10000000, Y).

size(null, 0) :- !.
key  (tree(K, _, _, _, _, _), K).
value(tree(_, V, _, _, _, _), V).
y    (tree(_, _, Y, _, _, _), Y).
left (tree(_, _, _, L, _, _), L).
right(tree(_, _, _, _, R, _), R).
size (tree(_, _, _, _, _, S), S).

build(K, V, Y, L, R, tree(K, V, Y, L, R, S)) :- size(L, LS), size(R, RS), S is LS + RS + 1.

split(_, null, _, _) :- !.
split(null, _, null, null) :- !.
split(tree(K, V, Y, L, R, _), J, Res1, Res2) :- K < J, !, split(R, J, T, Res2), build(K, V, Y, L, T, Res1).
% :NOTE: Упростить
% fixed
split(tree(K, V, Y, L, R, _), J, Res1, Res2) :- split(L, J, Res1, T), build(K, V, Y, T, R, Res2).

merge(A, null, A) :- !.
merge(null, B, B) :- !.
merge(tree(K1, V1, Y1, L1, R1, _), Tr2, Res) :- y(Tr2, Y2), Y1 > Y2, !, merge(R1, Tr2, T), build(K1, V1, Y1, L1, T, Res).
merge(Tr1, tree(K2, V2, Y2, L2, R2, _), Res) :- merge(Tr1, L2, T), build(K2, V2, Y2, T, R2, Res).

insert(null, N, N) :- !.
insert(Tr, tree(Ik, Iv, Iy, Il, Ir, _), Res) :-
    y(Tr, Y), Iy > Y, !, split(Tr, Ik, T1, T2), build(Ik, Iv, Iy, T1, T2, Res).
insert(tree(K, V, Y, L, R, _), Ins, Res) :-
    key(Ins, Ik), Ik < K, !, insert(L, Ins, Tmp), build(K, V, Y, Tmp, R, Res).
insert(tree(K, V, Y, L, R, _), Ins, Res) :-
    insert(R, Ins, Tmp), build(K, V, Y, L, Tmp, Res).

map_get(tree(K, V, _, _, _, _), K, V) :- !.
map_get(Tr, Key, Value) :- key(Tr, K), Key > K, !, right(Tr, R), map_get(R, Key, Value).
map_get(Tr, Key, Value) :- key(Tr, K), Key < K, left(Tr, L), map_get(L, Key, Value).

get_or_default(null, A, A) :- !.
get_or_default(A, B, A).

next_key(null, _, null) :- !.
next_key(Tr, Key, Ans) :- key(Tr, K), K > Key, !, left(Tr, L), next_key(L, Key, Ans1), get_or_default(Ans1, K, Ans).
next_key(Tr, Key, Ans) :- right(Tr, R), next_key(R, Key, Ans).

map_remove(TreeMap, Key, Result) :-
% :NOTE: Key1 is Key + 1
% fixed
    next_key(TreeMap, Key, Key1), split(TreeMap, Key, Res1, Res2),
    split(Res2, Key1, _, Res3), merge(Res1, Res3, Result).

map_put(TreeMap, Key, Value, Result) :-
    map_remove(TreeMap, Key, Tmp), node(Key, Value, Node), insert(Tmp, Node, Result).

build_tree([], Res, Res) :- !.
build_tree([(K, V) | T], TmpRes, Res) :- map_put(TmpRes, K, V, Tmp), build_tree(T, Tmp, Res).

map_build(ListMap, TreeMap) :- build_tree(ListMap, null, TreeMap).

map_subMapSize(null, _, _, 0) :- !.
map_subMapSize(_, FromKey, ToKey, 0) :- FromKey >= ToKey, !.
% :NOTE: Упростить
% fixed
map_subMapSize(Map, FromKey, ToKey, Size) :-
    split(Map, FromKey, _, Res1), split(Res1, ToKey, Res2, _), size(Res2, Size).

map_minKey(Map, Key) :- left(Map, null), !, key(Map, Key).
map_minKey(Map, Key) :- left(Map, L), map_minKey(L, Key).

map_maxKey(Map, Key) :- right(Map, null), !, key(Map, Key).
map_maxKey(Map, Key) :- right(Map, R), map_maxKey(R, Key).

map_floorKey(Map, Key, Key) :- split(Map, Key, _, Tmp), map_minKey(Tmp, Key), !.
map_floorKey(Map, Key, Ans) :- split(Map, Key, Tmp, _), map_maxKey(Tmp, Ans).

replace_in_node(null, _, null) :- !.
replace_in_node(tree(K, V, Y, null, null, 1), NewKey, tree(NewKey, V, Y, null, null, 1)).

map_replace(Map, Key, Value, Result) :- next_key(Map, Key, Key1),
		split(Map, Key, R1, R2), split(Map, Key1, R3, R4), replace_in_node(Node, Value, NewNode),
		merge(NewNode, R3, Tmp), merge(R1, Tmp, Result).
