// This file is to run through day 8 of advent of code, which is a repeating vector puzzle that
// requires some mental gymnastics to understand the source question...
package main

import (
  "bufio"
  "flag"
  "fmt"
  "os"
  "regexp"
)

// ValidLineCheck - The test we perform on each input string line to see if it's a valid input
const ValidLineCheck string = `^[0-9a-zA-Z.]+$`

// debug - Choose whether to run the program in debug mode
var debug = false

// Print out a given line if debug is enabled during the runtime of this program
func debugLine(lineToDebug string) {
  if debug {
    fmt.Println(lineToDebug)
  }
}

// Read in a given file and return each test statement in a slice
func readFile(filename string) ([]string, error) {
  var lineMap []string

  file, err := os.Open(filename)
  if err != nil {
    return lineMap, err
  }
  defer file.Close()

  var lineRegex = regexp.MustCompile(ValidLineCheck)

  scanner := bufio.NewScanner(file)
  for scanner.Scan() {
    var line = scanner.Text()
    if lineRegex.MatchString(line) {
      lineMap = append(lineMap, line)
    }
  }
  return lineMap, nil
}

// Main function to kick the work
func main() {
  // Do some initial CLI parsing to figure out what the requested operation is.
  var filename string
  flag.StringVar(&filename, "i", "input.txt", "Specify input file for the program")
  flag.BoolVar(&debug, "debug", false, "Enable debug logging")
  flag.Parse()

  // Read in the given file as a number of lines.
  lineMap, err := readFile(filename)
  if err != nil {
    fmt.Println(err)
    os.Exit(1)
  }

  debugLine(fmt.Sprintf("%v", lineMap))
}
