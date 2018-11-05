# ICDB-completeness

## test time without pre primary keys
Generate ordered tuple list for salary table:46.354s

Create ICDB-completeness csv file: 1795.561s=30min

Load salaries_comp table time: 533.048s

## test time with pre primary keys 

Create ordered tuple list done in: 46.484s

Generate all signatures time: 1794.779s

Generate ICDB-completeness csv file time: 21.298s

Load table time: 558.904s

## test time with pre and succ primary keys 
Create ordered tuple list done in: 49364ms

Generating signature...
Generate all signatures time: 1809610ms

Creating csv file...
Generate ICDB-completeness csv file time: 26218ms

Using database employees_icdb_completeness...
Load table time: 563274ms

## SELECTION test
### Test number of tuples: 20
Collecting Ts (Query return set) list... done
ts (Query Return set) collecting time: 3414ms

Collecting Tn list... done
tn (predecessor/successor  looking-for time: 1ms

Verification result: true
Verification time: 11ms

### Test number of tuples: 200
Collecting Ts (Query return set) list... done
ts (Query Return set) collecting time: 3524ms

Collecting Tn list... done
tn (predecessor/successor  looking-for time: 1ms

Verification result: true
Verification time: 28ms

### Test number of tuples: 2000
Collecting Ts (Query return set) list... done
ts (Query Return set) collecting time: 5568ms

Collecting Tn list... done
tn (predecessor/successor  looking-for time: 2ms

Verification result: true
Verification time: 94ms

### Test number of tuples: 20000
Collecting Ts (Query return set) list... done
ts (Query Return set) collecting time: 9444ms

Collecting Tn list... done
tn (predecessor/successor  looking-for time: 1ms

Verification result: true
Verification time: 345ms

### Test number of tuples: 100000
Collecting Ts (Query return set) list... done
ts (Query Return set) collecting time: 13386ms

Collecting Tn list... done
tn (predecessor/successor  looking-for time: 1ms

Verification result: true
Verification time: 1293ms

### Test number of tuples: 160000
Collecting Ts (Query return set) list... done
ts (Query Return set) collecting time: 15733ms

Collecting Tn list... done
tn (predecessor/successor  looking-for time: 1ms

Verification result: true
Verification time: 1739ms

### Test number of tuples: 200000
Collecting Ts (Query return set) list... done
ts (Query Return set) collecting time: 17204ms

Collecting Tn list... done
tn (predecessor/successor  looking-for time: 1ms

Verification result: true
Verification time: 2139ms





