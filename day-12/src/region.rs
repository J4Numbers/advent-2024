use std::collections::HashMap;
use log::log;

#[derive(Hash, Eq, PartialEq, Debug, Clone, Copy)]
struct Coordinate {
    x: i8,
    y: i8,
}

#[derive(Hash, Eq, PartialEq, Debug)]
struct SubRegion {
    exposed_sides: usize,
    coordinate: Coordinate,
}

#[derive(Hash, Eq, PartialEq, Debug)]
struct RegionMap {
    region_code: char,
    sub_regions: Vec<SubRegion>,
}

fn test_coord_in_map(coordinate: Coordinate, max_height: usize, max_width: usize) -> bool {
    coordinate.y >= 0 && coordinate.y < max_height as i8 &&
        coordinate.x >= 0 && coordinate.x < max_width as i8
}

fn explore_region(region_base_map: &Vec<Vec<char>>, start_loc: Coordinate) -> RegionMap {
    let mut explored_coords: Vec<Coordinate> = vec![];
    let mut coords_to_explore: Vec<Coordinate> = vec![start_loc];
    let mut sub_regions: Vec<SubRegion> = vec![];
    let region_code = *region_base_map.get(start_loc.y as usize).unwrap().get(start_loc.x as usize).unwrap();

    while !coords_to_explore.last().is_none() {
        let coord_to_test = coords_to_explore.pop().unwrap();
        let mut exposed_sides: usize = 0;

        let up = Coordinate{ x: coord_to_test.x, y: coord_to_test.y - 1 };
        if test_coord_in_map(up, region_base_map.len(), region_base_map.get(0).unwrap().len())
          && *region_base_map.get(up.y as usize).unwrap().get(up.x as usize).unwrap() == region_code {
            if  !explored_coords.contains(&up) && !coords_to_explore.contains(&up) {
                coords_to_explore.push(up)
            }
        } else {
            exposed_sides += 1;
        }

        let down = Coordinate{ x: coord_to_test.x, y: coord_to_test.y + 1 };
        if test_coord_in_map(down, region_base_map.len(), region_base_map.get(0).unwrap().len())
            && *region_base_map.get(down.y as usize).unwrap().get(down.x as usize).unwrap() == region_code {
            if  !explored_coords.contains(&down) && !coords_to_explore.contains(&down) {
                coords_to_explore.push(down)
            }
        } else {
            exposed_sides += 1;
        }

        let left = Coordinate{ x: coord_to_test.x - 1, y: coord_to_test.y };
        if test_coord_in_map(left, region_base_map.len(), region_base_map.get(0).unwrap().len())
            && *region_base_map.get(left.y as usize).unwrap().get(left.x as usize).unwrap() == region_code {
            if !explored_coords.contains(&left) && !coords_to_explore.contains(&left) {
                coords_to_explore.push(left)
            }
        } else {
            exposed_sides += 1;
        }

        let right = Coordinate{ x: coord_to_test.x + 1, y: coord_to_test.y };
        if test_coord_in_map(right, region_base_map.len(), region_base_map.get(0).unwrap().len())
            && *region_base_map.get(right.y as usize).unwrap().get(right.x as usize).unwrap() == region_code {
            if !explored_coords.contains(&right) && !coords_to_explore.contains(&right) {
                coords_to_explore.push(right)
            }
        } else {
            exposed_sides += 1;
        }

        sub_regions.push(SubRegion{
            exposed_sides,
            coordinate: coord_to_test,
        });
        explored_coords.push(coord_to_test);
    }

    RegionMap{
        region_code,
        sub_regions,
    }
}

fn test_coord_in_regions(region_map: &Vec<RegionMap>, coordinate: Coordinate) -> bool {
    let mut found = false;
    for region in region_map.iter() {
        for subregion in region.sub_regions.iter() {
            if subregion.coordinate == coordinate {
                found = true;
                break;
            }
        }
    }
    found
}

fn calculate_region_cost(region_map: &RegionMap) -> u64 {
    let area = region_map.sub_regions.len();
    let perimeter = region_map.sub_regions.iter().fold(0, |a, b| a + b.exposed_sides);

    log::debug!("Found region name {:?} with area of {:?} and perimeter of {:?}", region_map.region_code, area, perimeter);
    area as u64 * perimeter as u64
}

fn calculate_total_region_cost(region_map: &Vec<RegionMap>) -> u64 {
    let mut total_cost = 0;
    for region in region_map.iter() {
        total_cost += calculate_region_cost(region);
    }
    total_cost
}

pub(crate) fn explore_regions(region_map: &Vec<Vec<char>>) -> u64 {
    let mut discovered_regions: Vec<RegionMap> = vec![];

    for (yIdx, row) in region_map.iter().enumerate() {
        for (xIdx, cell) in row.iter().enumerate() {
            let curr_coord = Coordinate{ x: xIdx as i8, y: yIdx as i8 };
            if !test_coord_in_regions(&discovered_regions, curr_coord) {
                discovered_regions.push(explore_region(region_map, curr_coord));
            }
        }
    }

    calculate_total_region_cost(&discovered_regions)
}