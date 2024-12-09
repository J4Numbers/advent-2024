// This file is to run through day 9 of advent of code, which is an optimisation program to
// collapse free space down on a disk drive and generate a checksum...
package main

import (
  "bufio"
  "flag"
  "fmt"
  "os"
  "regexp"
  "sort"
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

// FileLocation - A marker which informs where a file is located within our disk
type FileLocation struct {
  id       int
  length   int
  startIdx int
}

// Read in a given file and return each test statement in a slice
func readFile(filename string) ([]FileLocation, error) {
  var fileColl []FileLocation

  file, err := os.Open(filename)
  if err != nil {
    return fileColl, err
  }
  defer file.Close()

  var lineRegex = regexp.MustCompile(ValidLineCheck)
  var fileLen = true
  var runningIdx = 0
  var foundFileId = 0

  scanner := bufio.NewScanner(file)
  for scanner.Scan() {
    var line = scanner.Text()
    if lineRegex.MatchString(line) {
      for idx := range line {
        // For each character, pull out the number
        var diskLen, _ = strconv.Atoi(string(line[idx]))

        // If it's a file length, then add the new file to our list
        if fileLen {
          fileColl = append(
            fileColl,
            FileLocation{id: foundFileId, length: diskLen, startIdx: runningIdx},
          )
          debugLine(
            fmt.Sprintf(
              "Found new file (%v) at idx %v of length %v",
              foundFileId, runningIdx, diskLen))
          foundFileId += 1
        }

        // Otherwise, keep track of the disk index and swap whether we're looking at file or free
        // length
        runningIdx += diskLen
        fileLen = !fileLen
      }
    }
  }

  return fileColl, nil
}

// Return a fragmented disk where files have been split into the earliest possible location they
// could go as partial blocks.
func fragmentDisk(sourceDisk []FileLocation) []FileLocation {
  // First, create the working slices to contain the disk we're iterating over
  var optimisedDisk []FileLocation
  workingDisk := make([]FileLocation, len(sourceDisk))
  copy(workingDisk, sourceDisk)

  // Starter variables that will change over time
  var blocksToFill = 0
  var lastIdx = 0

  // For each item in our disk...
  for idx := range workingDisk {
    // If the range is still valid (if not then break)
    if idx >= len(workingDisk) {
      break
    }

    // Check how many free blocks we could fill
    blocksToFill = workingDisk[idx].startIdx - lastIdx
    for {
      // If that count is 0 or the item we're looking at no-longer exists, then break out, otherwise
      // fit in some blocks
      if blocksToFill < 1 || idx >= len(workingDisk) {
        break
      }
      debugLine(fmt.Sprintf("Found %v remaining blocks to fill", blocksToFill))

      // Look at the last file in our list and see how much of that could fit into our freed up
      // blocks
      revIdx := len(workingDisk) - 1
      var revFileId = workingDisk[revIdx].id
      var cutLen int

      debugLine(fmt.Sprintf(
        "Moving file %v with length of %v",
        revFileId, workingDisk[revIdx].length))

      // For fitting partially, fit as much as we can and reduce the length of the file at the
      // end
      if workingDisk[revIdx].length > blocksToFill {
        cutLen = blocksToFill
        workingDisk[revIdx].length = workingDisk[revIdx].length - blocksToFill
      } else {
        // For fitting wholly, fit the whole file and remove the old marker from the end of the
        // slice
        cutLen = workingDisk[revIdx].length
        workingDisk = workingDisk[:revIdx]
      }

      debugLine(
        fmt.Sprintf(
          "Shifting %v blocks of file (%v) to start from idx %v",
          cutLen, revFileId, lastIdx))
      // Shift the known blocks that can be moved into the disk, increment our running index, and
      // decrease the remaining blocks we could fill
      optimisedDisk = append(optimisedDisk, FileLocation{
        id:       revFileId,
        length:   cutLen,
        startIdx: lastIdx,
      })
      lastIdx += cutLen
      blocksToFill -= cutLen
    }

    // If, by the time we reach the item we found the block of free space between, it has been
    // moved, then skip
    if idx < len(workingDisk) {

      // Otherwise, add this current file into our optimised disk as it is
      lastIdx = workingDisk[idx].startIdx + workingDisk[idx].length
      debugLine(
        fmt.Sprintf(
          "Inserting file (%v) at idx %v of length %v",
          workingDisk[idx].id, workingDisk[idx].startIdx, workingDisk[idx].length))
      optimisedDisk = append(optimisedDisk, FileLocation{
        id:       workingDisk[idx].id,
        length:   workingDisk[idx].length,
        startIdx: workingDisk[idx].startIdx,
      })
    }
  }

  return optimisedDisk
}

// Reorder the disk with whole files appearing at their earliest possible block.
func reorderDisk(sourceDisk []FileLocation) []FileLocation {
  // Only work on a copy of the source disk for this
  optimisedDisk := make([]FileLocation, len(sourceDisk))
  copy(optimisedDisk, sourceDisk)

  var blocksToFill = 0

  // From the last file in our block to the first...
  for idx := len(optimisedDisk) - 1; idx > -1; idx-- {
    var lastIdx = 0
    for revIdx := 0; revIdx <= idx; revIdx++ {
      // Try to find a space where the file could be placed
      blocksToFill = optimisedDisk[revIdx].startIdx - lastIdx
      debugLine(
        fmt.Sprintf(
          "Attempting to fit file (%v) of length %v into gap of %v before %v",
          optimisedDisk[idx].id, optimisedDisk[idx].length, blocksToFill, optimisedDisk[revIdx].id))

      // If we find a space, then change the index to that location and re-sort the slice to reflect
      // the correct order. Also offset the index by 1 to correct after sorting.
      if blocksToFill >= optimisedDisk[idx].length {
        debugLine(
          fmt.Sprintf(
            "Moving file (%v) to new start location (%v)",
            optimisedDisk[idx].id, lastIdx))
        optimisedDisk[idx].startIdx = lastIdx
        sort.Slice(optimisedDisk, func(i, j int) bool { return optimisedDisk[i].startIdx < optimisedDisk[j].startIdx })
        idx += 1
        break
      }

      // Move the last index up so that we are accurately tracking where we're looking in the
      // disk
      lastIdx = optimisedDisk[revIdx].startIdx + optimisedDisk[revIdx].length
    }
  }

  return optimisedDisk
}

// For each file we've found, calculate its checksum according to the ID by its held block index,
// all added together for every file.
func calculateChecksum(diskToChecksum []FileLocation) int {
  var checksum = 0
  for _, file := range diskToChecksum {
    for i := 0; i < file.length; i++ {
      checksum += file.id * (file.startIdx + i)
    }
  }
  return checksum
}

// Main function to kick the work
func main() {
  // Do some initial CLI parsing to figure out what the requested operation is.
  var filename string
  var complete bool
  flag.StringVar(&filename, "i", "input.txt", "Specify input file for the program")
  flag.BoolVar(&complete, "complete", false, "Only move files if they fit completely")
  flag.BoolVar(&debug, "debug", false, "Enable debug logging")
  flag.Parse()

  // Read in the given file as a number of lines.
  fileColl, err := readFile(filename)
  if err != nil {
    fmt.Println(err)
    os.Exit(1)
  }

  var optimisedColl []FileLocation

  if complete {
    optimisedColl = reorderDisk(fileColl)
  } else {
    optimisedColl = fragmentDisk(fileColl)
  }

  var checksum = calculateChecksum(optimisedColl)

  fmt.Println("Found checksum value of", checksum)
}
