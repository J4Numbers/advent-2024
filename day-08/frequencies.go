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

// Coordinate - How to represent a coordinate within our grid - and also how vectors are being
// represented because they're just a pair of numbers and I'm lazy...
type Coordinate struct {
  x int
  y int
}

// Read in a given file and return each test statement in a slice
func readFile(filename string) ([][]rune, map[rune][]Coordinate, error) {
  var freqMap [][]rune
  var frequencies = make(map[rune][]Coordinate)

  file, err := os.Open(filename)
  if err != nil {
    return freqMap, frequencies, err
  }
  defer file.Close()

  var lineRegex = regexp.MustCompile(ValidLineCheck)
  var numberRegex = regexp.MustCompile(`[0-9a-zA-Z]`)

  var lineIdx = 0

  scanner := bufio.NewScanner(file)
  for scanner.Scan() {
    var line = scanner.Text()
    if lineRegex.MatchString(line) {
      // Add a new blank line to the map
      freqLine := make([]rune, len(line))
      for i := 0; i < len(line); i++ {
        freqLine[i] = '.'
      }
      freqMap = append(freqMap, freqLine)

      // Find all the frequencies in each line
      matches := numberRegex.FindAllStringIndex(line, -1)
      for _, match := range matches {
        freqMatch := ([]rune(line[match[0]:match[1]]))[0]
        debugLine(
          fmt.Sprintf(
            "Found match at idx %v/%v : %v",
            match[0], match[1], string(freqMatch)))

        // For each frequency, see if that frequency is already being tracked, and append the
        // new frequency location to the map of frequencies and their origins.
        _, ok := frequencies[freqMatch]
        if !ok {
          frequencies[freqMatch] = []Coordinate{}
        }
        frequencies[freqMatch] = append(frequencies[freqMatch], Coordinate{x: match[0], y: lineIdx})
      }

      lineIdx += 1
    }
  }
  return freqMap, frequencies, nil
}

// Test whether a coordinate is within a set of grid bounds.
func testBounds(testCoord Coordinate, maxHeight int, maxWidth int) bool {
  return testCoord.y > -1 && testCoord.y < maxHeight &&
      testCoord.x > -1 && testCoord.x < maxWidth
}

// Apply a vector difference repeatedly to an origin coordinate for as long as we're within some
// coordinate boundaries.
func repeatOnNode(origin Coordinate, diff Coordinate, maxHeight int, maxWidth int) []Coordinate {
  var validCandidates []Coordinate

  candidate := Coordinate{x: origin.x + diff.x, y: origin.y + diff.y}
  for {
    if !testBounds(candidate, maxHeight, maxWidth) {
      break
    }
    validCandidates = append(validCandidates, candidate)
    candidate = Coordinate{x: candidate.x + diff.x, y: candidate.y + diff.y}
  }

  return validCandidates
}

// Discover all associated antinodes for a set of originating coordinates, with the behaviour
// changing if we're looking at repeating frequencies.
func discoverAntinodes(freqs []Coordinate, repeat bool, maxHeight int, maxWidth int) []Coordinate {
  var antinodes []Coordinate

  // For each pair of originating frequency coordinates...
  for i := 0; i < len(freqs); i++ {
    for j := i + 1; j < len(freqs); j++ {

      // Work out the difference vector
      diff := Coordinate{x: freqs[i].x - freqs[j].x, y: freqs[i].y - freqs[j].y}
      debugLine(
        fmt.Sprintf(
          "Testing frequency %v against %v => %v",
          freqs[i], freqs[j], diff))

      // Apply the vector positively to the respective node (if we're repeating frequencies, then
      // the node changes) and append the resulting antinode(s) to the list of found nodes.
      if repeat {
        antinodes = append(antinodes, repeatOnNode(freqs[j], diff, maxHeight, maxWidth)...)
      } else {
        antinodes = append(antinodes, Coordinate{x: freqs[i].x + diff.x, y: freqs[i].y + diff.y})
      }

      // Apply the vector negatively to the respective node (if we're repeating frequencies, then
      // the node changes) and append the resulting antinode(s) to the list of found nodes.
      if repeat {
        antiDiff := Coordinate{x: diff.x * -1, y: diff.y * -1}
        antinodes = append(antinodes, repeatOnNode(freqs[i], antiDiff, maxHeight, maxWidth)...)
      } else {
        antinodes = append(antinodes, Coordinate{x: freqs[j].x - diff.x, y: freqs[j].y - diff.y})
      }
    }
  }
  return antinodes
}

// For the antinodes that we've been given, plot them into the base map we have as long as they
// are within its bounds, signifying that a location is receiving a signal by marking the square
// with a # sign
func plotAntinodes(baseMap [][]rune, antinodes map[rune][]Coordinate) {
  for _, nodes := range antinodes {
    for _, node := range nodes {
      if node.y > -1 && node.y < len(baseMap) &&
          node.x > -1 && node.x < len(baseMap[0]) {
        baseMap[node.y][node.x] = '#'
      }
    }
  }
}

// Count the number of nodes that are flagged as receiving a signal.
func countNodes(baseMap [][]rune) int {
  total := 0
  for _, line := range baseMap {
    for _, chr := range line {
      if chr == '#' {
        total += 1
      }
    }
  }
  return total
}

// Main function to kick the work
func main() {
  // Do some initial CLI parsing to figure out what the requested operation is.
  var filename string
  var repeat bool
  flag.StringVar(&filename, "i", "input.txt", "Specify input file for the program")
  flag.BoolVar(&repeat, "repeat", false, "Enable repeated appliance of vectors")
  flag.BoolVar(&debug, "debug", false, "Enable debug logging")
  flag.Parse()

  // Read in the given file as a number of lines.
  baseMap, frequencies, err := readFile(filename)
  if err != nil {
    fmt.Println(err)
    os.Exit(1)
  }

  // For each set of frequencies that we've discovered, pull out the set of antinodes that are
  // created from them.
  debugLine(fmt.Sprintf("Found %v frequencies to track", len(frequencies)))
  antinodes := make(map[rune][]Coordinate)
  for k, freqs := range frequencies {
    debugLine(fmt.Sprintf("Code %v has %v antennas", string(k), len(freqs)))
    antinodes[k] = discoverAntinodes(freqs, repeat, len(baseMap), len(baseMap[0]))
  }

  // Plot those antinodes into the map we've been given to reduce duplication of antinodes.
  plotAntinodes(baseMap, antinodes)

  // And count the total number of antinodes generated within that plotted map.
  totalNodes := countNodes(baseMap)

  fmt.Println("The total number of unique antinodes is", totalNodes)
}
