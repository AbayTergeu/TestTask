This repository for create 2 tasks:

Question 1 (33%)
A dairy farmer wishes to manage the breeding of the cows in his farm. For this reason, he defines the Cow entity (cowId, nickName). Cows can either give birth to calves by insemination or end their life span.
Your mission is to create a data structure to support the dairy farm and the following operations on it (assume you start with a single cow that is always alive, and that all calves are born female):
1. GiveBirth (parentCowId, childCowId, childNickName) – adds a new female calf to the farm.
2. EndLifeSpan (cowId) – removes the cow from the farm.
3. Print farm data – outputs entire farm to the standard output in a readable manner.

Question 2 (67%)
Repeat question 1 with the following limitation: you CAN NOT use Java built in arrays/collections/lists/maps (or any data structure which inherits or uses them). So, for instance using the brackets “[“or “]” anywhere is forbidden. You must implement your own data structure to support the same operations.



For the 2nd task, at first I wanted to use a self-written List and map, but without an array it is not clear where to store records with indexes.
As a result, I chose the cache, I store data there instead of an array.
