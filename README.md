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

# Generator

Request URL: `http://localhost:8080/generate`

Parameters:

| Name | Description |
| ---- |:----------- |
| state | See description in Solver. Only difference here is that the state must be a valid SOLVED state. |
| size | Integer, size = [4,9,16]. The size of a puzzle is equal to the number of cells in a row. For example a 9x9 puzzle has a size of 9. |
| maxSolutions | Integer, maxSolutions>0, default=1. This is to control the number of solutions a returned puzzle is allowed to have. |
| maxEmptyCells | Integer, maxEmptyCells>=0, default=0. This is to control the number of empty cells a returned puzzle is allowed to have. When set to 0, it is unlimited. |

# Example

Either state or size is required in a request and they are mutually exlusive:

GET with `size`: 

`http://localhost:8080/generate?size=9`

Response Body: 

```
{
    "puzzle": "006009030001200000000537200000080012600000578004000000005062007000950000040001000"
}
```

GET with `state`: 

`http://localhost:8080/generate?state=756389241493621875812547396268795134579134682134862759987416523341258967625973418`

Response Body: 

```
{
    "puzzle": "000080040000600005810040306200790034509000000000060000007000000301008900000900408"
}
```
