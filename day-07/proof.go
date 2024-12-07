// This file is to run through day 7 of advent of code, which is a proofing problem where the
// validity of several statements needs to be tested.
package main

import (
  "bufio"
  "flag"
  "fmt"
  "os"
  "regexp"
  "strconv"
  "strings"
)

// ValidLineCheck - The test we perform on each input string line to see if it's a valid input
const ValidLineCheck string = `^[0-9]+:( [0-9]+)+$`

// debug - Choose whether to run the program in debug mode
var debug = false

// joinOperator - Choose whether to enable the join operator
var joinOperator = false

// Print out a given line if debug is enabled during the runtime of this program
func debugLine(lineToDebug string) {
  if debug {
    fmt.Println(lineToDebug)
  }
}

// TestStatement - The component that describes a single line in the file, which is the target
// value we must reach, and the components that will make up that target.
type TestStatement struct {
  testValue  int
  components []int
}

// Read in a given file and return each test statement in a slice
func readFile(filename string) ([]TestStatement, error) {
  var testStatements []TestStatement

  file, err := os.Open(filename)
  if err != nil {
    return testStatements, err
  }
  defer file.Close()

  var lineRegex = regexp.MustCompile(ValidLineCheck)
  var numberRegex = regexp.MustCompile(`[0-9]+`)

  scanner := bufio.NewScanner(file)
  for scanner.Scan() {
    var line = scanner.Text()
    if lineRegex.MatchString(line) {
      var testValue int
      var components []int
      matches := numberRegex.FindAllString(line, -1)
      for idx, match := range matches {
        if idx == 0 {
          testValue, _ = strconv.Atoi(match)
        } else {
          newComp, _ := strconv.Atoi(match)
          components = append(components, newComp)
        }
      }
      debugLine(
        fmt.Sprintf(
          "Found new test statement for value %v with components %v",
          testValue, components))
      testStatements = append(testStatements, TestStatement{testValue: testValue, components: components})
    }
  }
  return testStatements, nil
}

// Test whether a provided statement can be created when its components are combined via
// multiplication, addition, or - when set - string joins. Return whether it is possible, and
// the operations required to create the value.
//
// Uses recursion to build up a value.
func testValidStatement(statement TestStatement) (bool, []string) {
  var testTotal = statement.testValue
  ops := make([]string, len(statement.components)-1)
  lastComponent := statement.components[len(statement.components)-1]
  // Base case
  if len(statement.components) == 1 {
    return testTotal == statement.components[0], ops
  }

  // Test if multiply is possible first... _THEN_ see if the tree below it can be reached via
  // multiplication
  if (statement.testValue % lastComponent) == 0 {
    mulValid, preOps := testValidStatement(TestStatement{testValue: testTotal / lastComponent, components: statement.components[0 : len(statement.components)-1]})
    if mulValid {
      ops = append(preOps, "mul")
      return mulValid, ops
    }
  }

  if joinOperator {
    // If the test value ends with our current component, it's a candidate for the join operator
    // too
    if strings.HasSuffix(strconv.Itoa(statement.testValue), strconv.Itoa(lastComponent)) {
      // Strip the length of our current component off of the test value and pass what's left down
      // to see if that can be made valid.
      strippedVal, _ := strconv.Atoi(strconv.Itoa(statement.testValue)[0 : len(strconv.Itoa(statement.testValue))-len(strconv.Itoa(lastComponent))])
      joinValid, preOps := testValidStatement(TestStatement{
        testValue:  strippedVal,
        components: statement.components[0 : len(statement.components)-1],
      })
      if joinValid {
        ops = append(preOps, "join")
        return joinValid, ops
      }
    }
  }

  // If no other case fits, see what happens when the last element is removed from the test total
  // and continue down the tree.
  addValid, preOps := testValidStatement(TestStatement{testValue: testTotal - lastComponent, components: statement.components[0 : len(statement.components)-1]})
  ops = append(preOps, "add")
  return addValid, ops
}

// Main function to kick the work
func main() {
  // Do some initial CLI parsing to figure out what the requested operation is.
  var filename string
  flag.StringVar(&filename, "i", "input.txt", "Specify input file for the program")
  flag.BoolVar(&joinOperator, "join", false, "Enable join operator alongside mul and add")
  flag.BoolVar(&debug, "debug", false, "Enable debug logging")
  flag.Parse()

  // Read in the given file as a number of lines.
  testStatements, err := readFile(filename)
  if err != nil {
    fmt.Println(err)
    os.Exit(1)
  }

  var validStatementTotal = 0

  // For each statement that we've found, test whether it's valid and if it is, add that test
  // value onto our running total.
  for _, statement := range testStatements {
    validStatement, ops := testValidStatement(statement)
    if validStatement {
      debugLine(
        fmt.Sprintf(
          "Components %v reached test value of %v via operations %v",
          statement.components, statement.testValue, ops))
      validStatementTotal += statement.testValue
    }
  }

  fmt.Println("The total value for all valid statements is", validStatementTotal)
}
