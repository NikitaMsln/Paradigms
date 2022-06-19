put_min_divisor(N, _) :- min_divisor(N, X), !.
put_min_divisor(N, I) :- assertz(min_divisor(N, I)).

build_table(I, J, N) :- J < N, assertz(composite(J)), put_min_divisor(J, I), J1 is J + I, build_table(I, J1, N).
build_table(I, N) :- \+ composite(I), J is I * I, build_table(I, J, N).
build_table(I, N) :- I * I < N, I1 is I + 1, build_table(I1, N).

init(MAX_N) :- (build_table(2, MAX_N); true).

prime(N) :- N > 1, \+ composite(N).

is_min_divisor(P, P) :- prime(P), !.
is_min_divisor(N, P) :- min_divisor(N, P).

is_sorted_list_of_primes([]).
is_sorted_list_of_primes([N]) :- prime(N).
is_sorted_list_of_primes([H1, H2 | T]) :- \+ (H1 > H2), prime(H1), is_sorted_list_of_primes([H2 | T]).

prod([], 1).
prod([H | T], R) :- prod(T, R1), R is R1 * H.

prime_divisors(1, []) :- !.
prime_divisors(N, [H | T]) :- number(N), !, is_min_divisor(N, H), N1 is div(N, H), prime_divisors(N1, T).
prime_divisors(N, L) :- is_sorted_list_of_primes(L), prod(L, N).

merge([], L, L) :- !.
merge(L, [], L) :- !.
merge([H | T1], [H | T2], [H | T]) :- merge(T1, T2, T).
merge([H1 | T1], [H2 | T2], [H1 | T]) :- H1 < H2, merge(T1, [H2 | T2], T).
merge([H1 | T1], [H2 | T2], [H2 | T]) :- H1 > H2, merge([H1 | T1], T2, T).

lcm(A, B, R) :- prime_divisors(A, X1), prime_divisors(B, X2), merge(X1, X2, X), prod(X, R).
