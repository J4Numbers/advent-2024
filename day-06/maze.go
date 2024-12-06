// This file is to run through day 6 of advent of code, which is more or less a maze simulation
// with a set of rules as to how the maze is traversed.
package main

import (
  "bufio"
  "flag"
  "fmt"
  "os"
  "regexp"
  "slices"
)

// ValidLineCheck - The test we perform on each input string line to see if it's a valid input
const ValidLineCheck string = `^[.#^]+$`

// debug - Choose whether to run the program in debug mode
var debug = false

// Print out a given line if debug is enabled during the runtime of this program
func debugLine(lineToDebug string) {
  if debug {
    fmt.Println(lineToDebug)
  }
}

type coord struct {
  x int
  y int
}

type movement struct {
  point     coord
  direction coord
}

// Read in a given file and return each line in a slice
func readFile(filename string) ([]string, error) {
  var fileContents []string

  file, err := os.Open(filename)
  if err != nil {
    return fileContents, err
  }
  defer file.Close()

  var lineRegex = regexp.MustCompile(ValidLineCheck)

  scanner := bufio.NewScanner(file)
  for scanner.Scan() {
    var line = scanner.Text()
    if lineRegex.MatchString(line) {
      fileContents = append(fileContents, line)
    }
  }
  return fileContents, nil
}

// Iterate over the split file and generate a basic map without the location of the start position.
// Return the blank map along with the coordinate of that stripped start position.
func generateBaseMap(fileMap []string) ([][]rune, coord) {
  var baseMap [][]rune
  var startLoc coord

  for yIdx, mapLine := range fileMap {
    var lineTrack []rune
    for xIdx, mapChr := range mapLine {
      if mapChr == '#' || mapChr == '.' {
        lineTrack = append(lineTrack, mapChr)
      } else if mapChr == '^' {
        lineTrack = append(lineTrack, 'X')
        startLoc = coord{x: xIdx, y: yIdx}
      }
    }
    baseMap = append(baseMap, lineTrack)
  }

  return baseMap, startLoc
}

// Test whether a coordinate is outside the grid we're working on. Return true if it's
// outside, false if the coordinate resides within the grid.
func outsideGrid(loc coord, dimensions coord) bool {
  return loc.x < 0 || loc.y < 0 ||
      loc.x >= dimensions.x ||
      loc.y >= dimensions.y
}

// Not the nicest function, but rotate the vector we're provided 90 degrees to the right in
// the context of our file grid.
func rotateVector(vectorToRotate coord) coord {
  if vectorToRotate.x == 1 {
    return coord{x: 0, y: 1}
  } else if vectorToRotate.x == -1 {
    return coord{x: 0, y: -1}
  } else if vectorToRotate.y == 1 {
    return coord{x: -1, y: 0}
  } else {
    return coord{x: 1, y: 0}
  }
}

// Test whether a map and starting point can be escaped. Return true if there is a way to escape
// the maze, and false otherwise.
func testEscapeable(baseMap [][]rune, startPoint coord, dimensions coord) bool {
  var vector = coord{x: 0, y: -1}
  var currentPos = startPoint
  var escapeable bool

  // Track the moves we've seen, which is a mix of the point and the direction allowing us to
  // already know when we're trapped in a loop.
  var visited []movement
  visited = append(visited, movement{point: startPoint, direction: vector})

  for {
    nextMove := movement{
      point:     coord{x: currentPos.x + vector.x, y: currentPos.y + vector.y},
      direction: vector,
    }

    // If we're outside the grid, the maze is escapeable.
    if outsideGrid(nextMove.point, dimensions) {
      escapeable = true
      break
    }
    // If the list of visited movements contains the planned movement, then we know we've already
    // seen it and that we're currently in a closed loop. Therefore, the maze cannot be escaped
    // from.
    if slices.ContainsFunc(visited, func(testMove movement) bool {
      return testMove.direction.x == nextMove.direction.x &&
          testMove.direction.y == nextMove.direction.y &&
          testMove.point.x == nextMove.point.x &&
          testMove.point.y == nextMove.point.y
    }) {
      escapeable = false
      break
    }

    debugLine(
      fmt.Sprintf(
        "Escape route check [%v,%v] > [%v,%v] - %v",
        nextMove.point.x, nextMove.point.y,
        nextMove.direction.x, nextMove.direction.y,
        string(baseMap[nextMove.point.y][nextMove.point.x])))

    // If the next point is an obstacle, don't move and just rotate with a new vector.
    if baseMap[nextMove.point.y][nextMove.point.x] == '#' {
      vector = rotateVector(vector)
      visited = append(visited, movement{
        point:     currentPos,
        direction: vector,
      })
    } else {
      // Otherwise, move forwards and register the movement.
      currentPos = nextMove.point
      visited = append(visited, nextMove)
    }
  }

  return escapeable
}

// Perform a regular walk through the base map, returning the updated map with visited locations
// and a list of potential obstacles that would cause an infinite loop.
func runWalk(baseMap [][]rune, startPoint coord, dimensions coord) ([][]rune, []coord) {
  var vector = coord{x: 0, y: -1}
  var currentPos = startPoint
  var obstPoints []coord

  for {
    nextPos := coord{x: currentPos.x + vector.x, y: currentPos.y + vector.y}

    // If the next point is outside the grid, we can stop searching along a path.
    if outsideGrid(nextPos, dimensions) {
      break
    }
    debugLine(
      fmt.Sprintf(
        "Investigating prospective space [%v,%v] - %v",
        nextPos.x, nextPos.y, string(baseMap[nextPos.y][nextPos.x])))

    if baseMap[nextPos.y][nextPos.x] == '#' {
      // If we hit an obstacle, turn.
      vector = rotateVector(vector)
    } else {
      // If it's not an obstacle, then check whether it's somewhere we've visited before, and if
      // not, attempt an escape with a new obstruction.
      if baseMap[nextPos.y][nextPos.x] == '.' {
        var newSlice [][]rune
        for _, line := range baseMap {
          newline := make([]rune, len(line))
          copy(newline, line)
          newSlice = append(newSlice, newline)
        }
        newSlice[nextPos.y][nextPos.x] = '#'
        debugLine(
          fmt.Sprintf(
            "Testing new escape route starting from [%v,%v] with new obstacle at [%v,%v]",
            startPoint.x, startPoint.y, nextPos.x, nextPos.y))
        // If the escape is successful, add the new obstacle to the list of potential obstacles.
        if !testEscapeable(newSlice, startPoint, dimensions) {
          debugLine(
            fmt.Sprintf("Escape successful starting from [%v,%v] with new obstacle at [%v,%v]",
              startPoint.x, startPoint.y, nextPos.x, nextPos.y))
          obstPoints = append(obstPoints, nextPos)
        }
        newSlice = nil
      }

      // For non-obstacle blocks, mark the current point as visited and move onto the next step.
      baseMap[nextPos.y][nextPos.x] = 'X'
      currentPos = nextPos
    }
  }

  return baseMap, obstPoints
}

// Count the number of X runes we can find within our map of patrols. This will give us the number
// of unique squares visited after a run of the program.
func countVisit(baseMap [][]rune) int {
  total := 0
  for _, line := range baseMap {
    for _, chr := range line {
      if chr == 'X' {
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
  flag.StringVar(&filename, "i", "input.txt", "Specify input file for the program")
  flag.BoolVar(&debug, "debug", false, "Enable debug logging")
  flag.Parse()

  // Read in the given file as a number of lines.
  fileContents, err := readFile(filename)
  if err != nil {
    fmt.Println(err)
    os.Exit(1)
  }

  // Generate some basic metadata about the map and its contents.
  baseMap, startPoint := generateBaseMap(fileContents)
  maxHeight := len(baseMap)
  maxWidth := len(baseMap[0])

  debugLine(fmt.Sprintf("Found max dimensions [%v,%v]", maxWidth, maxHeight))
  debugLine(fmt.Sprintf("Found starting position [%v,%v]", startPoint.x, startPoint.y))

  // Perform a walk on that map which will check how many unique spaces we visit, and how many
  // potential infinite loops we could create.
  newBase, obstCount := runWalk(baseMap, startPoint, coord{x: maxWidth, y: maxHeight})
  totalSpaces := countVisit(newBase)

  fmt.Println("The final count for visited spaces was", totalSpaces)
  fmt.Println("The number of potential obstacles for an infinite loop was", len(obstCount))
}
