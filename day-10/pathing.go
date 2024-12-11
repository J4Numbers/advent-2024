// This file is to run through day 9 of advent of code, which is an optimisation program to
// collapse free space down on a disk drive and generate a checksum...
package main

import (
  "bufio"
  "flag"
  "fmt"
  "os"
  "regexp"
  "slices"
  "strconv"
)

// ValidLineCheck - The test we perform on each input string line to see if it's a valid input
const ValidLineCheck string = `^[0-9]+$`

// debug - Choose whether to run the program in debug mode
var debug = false

// Print out a given line if debug is enabled during the runtime of this program
func debugLine(lineToDebug string) {
  if debug {
    fmt.Println(lineToDebug)
  }
}

// Coordinate - A marker in the grid
type Coordinate struct {
  x int
  y int
}

// TrailMarker - A marker which is used as a shorthand cache for tracking a location's height and
// the valid trails found for that marker (paths to a unique 9)
type TrailMarker struct {
  height         int
  checked        bool
  validTrailEnds []Coordinate
}

// Read in a given file and return a map of the trail
func readFile(filename string) ([][]TrailMarker, error) {
  var trailMap [][]TrailMarker

  file, err := os.Open(filename)
  if err != nil {
    return trailMap, err
  }
  defer file.Close()

  var lineRegex = regexp.MustCompile(ValidLineCheck)
  var lineCount = 0

  scanner := bufio.NewScanner(file)
  for scanner.Scan() {
    var line = scanner.Text()
    if lineRegex.MatchString(line) {
      var trailList []TrailMarker

      for idx := range line {
        // For each character, pull out the number
        var height, _ = strconv.Atoi(string(line[idx]))

        // State that we have not checked the current position, unless it is a 9, which is
        // implicitly the end of a trail.
        var checked = false
        var validTrails []Coordinate

        if height == 9 {
          checked = true
          validTrails = append(validTrails, Coordinate{
            x: idx,
            y: lineCount,
          })
        }

        trailList = append(trailList, TrailMarker{
          height:         height,
          checked:        checked,
          validTrailEnds: validTrails,
        })
      }

      trailMap = append(trailMap, trailList)
      lineCount += 1
    }
  }

  return trailMap, nil
}

// Test whether a coordinate is valid and can be checked in our map.
func testCoord(coordinate Coordinate, maxX int, maxY int) bool {
  return coordinate.x >= 0 && coordinate.x < maxX &&
    coordinate.y >= 0 && coordinate.y < maxY
}

// Perform a recursive investigation of how many different endings can be reached by a given
// node in our graph.
func investigateTrail(trailMap [][]TrailMarker, start Coordinate) {
  var startHeight = trailMap[start.y][start.x].height

  debugLine(
    fmt.Sprintf(
      "Investigating trail starting from %v, which has a height of %v",
      start, startHeight))

  // The steps are the same, so test the coordinate above this one to see if the height is an
  // incremental step.
  var up = Coordinate{x: start.x, y: start.y - 1}
  if testCoord(up, len(trailMap[0]), len(trailMap)) {
    debugLine(fmt.Sprintf("New co-ordinate to check - %v", up))
    if trailMap[up.y][up.x].height == startHeight+1 {
      // If this is a valid hop, then check to see if this node has been mapped out. If it hasn't,
      // do that mapping now.
      if !trailMap[up.y][up.x].checked {
        investigateTrail(trailMap, up)
      }
      // Once the mapping has been confirmed, add all trail ends to the current node.
      trailMap[start.y][start.x].validTrailEnds = append(trailMap[start.y][start.x].validTrailEnds, trailMap[up.y][up.x].validTrailEnds...)
    }
  }

  var down = Coordinate{x: start.x, y: start.y + 1}
  if testCoord(down, len(trailMap[0]), len(trailMap)) {
    debugLine(fmt.Sprintf("New co-ordinate to check - %v", down))
    if trailMap[down.y][down.x].height == startHeight+1 {
      if !trailMap[down.y][down.x].checked {
        investigateTrail(trailMap, down)
      }
      trailMap[start.y][start.x].validTrailEnds = append(trailMap[start.y][start.x].validTrailEnds, trailMap[down.y][down.x].validTrailEnds...)
    }
  }

  var left = Coordinate{x: start.x - 1, y: start.y}
  if testCoord(left, len(trailMap[0]), len(trailMap)) {
    debugLine(fmt.Sprintf("New co-ordinate to check - %v", left))
    if trailMap[left.y][left.x].height == startHeight+1 {
      if !trailMap[left.y][left.x].checked {
        investigateTrail(trailMap, left)
      }
      trailMap[start.y][start.x].validTrailEnds = append(trailMap[start.y][start.x].validTrailEnds, trailMap[left.y][left.x].validTrailEnds...)
    }
  }

  var right = Coordinate{x: start.x + 1, y: start.y}
  if testCoord(right, len(trailMap[0]), len(trailMap)) {
    debugLine(fmt.Sprintf("New co-ordinate to check - %v", right))
    if trailMap[right.y][right.x].height == startHeight+1 {
      if !trailMap[right.y][right.x].checked {
        investigateTrail(trailMap, right)
      }
      trailMap[start.y][start.x].validTrailEnds = append(trailMap[start.y][start.x].validTrailEnds, trailMap[right.y][right.x].validTrailEnds...)
    }
  }

  // Now we have found all valid trail ends that can be reached, do a simple sort to make sure
  // identical ends are a peer to one another.
  slices.SortFunc(trailMap[start.y][start.x].validTrailEnds, func(a, b Coordinate) int {
    if a.y < b.y {
      return -1
    }
    if a.y > b.y {
      return 1
    }
    return a.x - b.x
  })

  // And mark this node as checked.
  trailMap[start.y][start.x].checked = true
}

// Investigate all starting points within our map and fill out the different endpoints that can
// be found.
func findValidTrails(trailMap [][]TrailMarker) {
  for yIdx := range trailMap {
    for xIdx := range trailMap[yIdx] {
      // Go through all nodes in our map and perform a complete investigation...
      if !trailMap[yIdx][xIdx].checked {
        investigateTrail(trailMap, Coordinate{x: xIdx, y: yIdx})
      }
    }
  }
}

// Given a fully investigated map, return the score of the map according to whether we're running
// part one or part two.
func countValidTrails(trailMap [][]TrailMarker, paths bool) int {
  var count = 0

  for yIdx := range trailMap {
    for xIdx := range trailMap[yIdx] {
      if trailMap[yIdx][xIdx].height == 0 {
        if paths {
          // If we're running part two, just return the number of valid ends.
          count += len(trailMap[yIdx][xIdx].validTrailEnds)
        } else {
          // If we're running part one, remove duplicates first.
          count += len(slices.Compact(trailMap[yIdx][xIdx].validTrailEnds))
        }
      }
    }
  }

  return count
}

// Main function to kick the work
func main() {
  // Do some initial CLI parsing to figure out what the requested operation is.
  var filename string
  var paths bool
  flag.StringVar(&filename, "i", "input.txt", "Specify input file for the program")
  flag.BoolVar(&paths, "paths", false, "Enable path-based counts")
  flag.BoolVar(&debug, "debug", false, "Enable debug logging")
  flag.Parse()

  // Read in the given file as a number of lines.
  trailMap, err := readFile(filename)
  if err != nil {
    fmt.Println(err)
    os.Exit(1)
  }

  // Map out all the trails and the different ends that can be reached
  findValidTrails(trailMap)

  // Count the total valid trails according to whether we're looking for distinct endpoints or
  // distinct paths.
  totalTrails := countValidTrails(trailMap, paths)

  fmt.Println("Found total trail score of", totalTrails)
}
