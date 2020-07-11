# sudoku-webservice

This is a Sudoku Rest API created using Spring Boot for personal education purposes.

Since it is currently not deployed anywhere, so this documentation will use localhost URLs.

# Solver

Request URL: `http://localhost:8080/solve`

Parameters:

| Name | Description |
| ---- |:----------- |
| state | String, '0123...'. The parameter state is the String representation of the puzzle state using 0-9 & A-Z (or a-z) in order, where 0 represents an empty cell. For Example, size 4 puzzles will only be allowed 0-4, size 16 puzzles will use 1-9 and A-G. The length of State is equal to the number of cells in puzzle, size 9 puzzles have 9x9 = 81 cells. It is also not allowed to violate any Sudoku rules such as repeating values (except 0) in the same row, column or block. |
| n | Integer, n>0. The parameter n is the maximum number of solutions to look for in a given puzzle. Note that solvers are also limited by an internal maximum step count of 5000. |

Response:

| Name | Description |
| ---- |:----------- |
| solutions | List of solutions for the given state up to length n. |
| isComplete | True if solver has found every possible solution. Otherwise, the number of solutions is greater than n and/or the query used up its resource quota before completing the search. |

The information above can also be obtained via a GET request to: `http://localhost:8080/solve/info`

# Example

GET: 

`http://localhost:8080/solve/?state=200080300060070084030500209000105408000000000402706000301007040720040060004010003&n=2`

Response Body: 

```
{
    "solutions": [
        "245981376169273584837564219976125438513498627482736951391657842728349165654812793"
    ],
    "isComplete": true
}
```
