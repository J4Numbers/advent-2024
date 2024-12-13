#[derive(Hash, Eq, PartialEq, Debug, Clone, Copy)]
struct Coordinate {
    x: i32,
    y: i32,
}

#[derive(Hash, Eq, PartialEq, Debug)]
struct ExposedSides {
    up: bool,
    down: bool,
    left: bool,
    right: bool,
}

#[derive(Hash, Eq, PartialEq, Debug)]
struct SubRegion {
    exposed_sides: ExposedSides,
    coordinate: Coordinate,
}

#[derive(Hash, Eq, PartialEq, Debug)]
struct RegionMap {
    region_code: char,
    sub_regions: Vec<SubRegion>,
}

fn test_coord_in_map(coordinate: Coordinate, max_height: usize, max_width: usize) -> bool {
    coordinate.y >= 0 && coordinate.y < max_height as i32 &&
        coordinate.x >= 0 && coordinate.x < max_width as i32
}

fn explore_region(region_base_map: &Vec<Vec<char>>, start_loc: Coordinate) -> RegionMap {
    let mut explored_coords: Vec<Coordinate> = vec![];
    let mut coords_to_explore: Vec<Coordinate> = vec![start_loc];
    let mut sub_regions: Vec<SubRegion> = vec![];
    let region_code = *region_base_map.get(start_loc.y as usize).unwrap().get(start_loc.x as usize).unwrap();

    while !coords_to_explore.last().is_none() {
        let coord_to_test = coords_to_explore.pop().unwrap();
        let mut exposed_sides = ExposedSides{up: false, down: false, left: false, right: false};

        let up = Coordinate{ x: coord_to_test.x, y: coord_to_test.y - 1 };
        if test_coord_in_map(up, region_base_map.len(), region_base_map.get(0).unwrap().len())
          && *region_base_map.get(up.y as usize).unwrap().get(up.x as usize).unwrap() == region_code {
            if  !explored_coords.contains(&up) && !coords_to_explore.contains(&up) {
                coords_to_explore.push(up)
            }
        } else {
            exposed_sides.up = true;
        }

        let down = Coordinate{ x: coord_to_test.x, y: coord_to_test.y + 1 };
        if test_coord_in_map(down, region_base_map.len(), region_base_map.get(0).unwrap().len())
            && *region_base_map.get(down.y as usize).unwrap().get(down.x as usize).unwrap() == region_code {
            if  !explored_coords.contains(&down) && !coords_to_explore.contains(&down) {
                coords_to_explore.push(down)
            }
        } else {
            exposed_sides.down = true;
        }

        let left = Coordinate{ x: coord_to_test.x - 1, y: coord_to_test.y };
        if test_coord_in_map(left, region_base_map.len(), region_base_map.get(0).unwrap().len())
            && *region_base_map.get(left.y as usize).unwrap().get(left.x as usize).unwrap() == region_code {
            if !explored_coords.contains(&left) && !coords_to_explore.contains(&left) {
                coords_to_explore.push(left)
            }
        } else {
            exposed_sides.left = true;
        }

        let right = Coordinate{ x: coord_to_test.x + 1, y: coord_to_test.y };
        if test_coord_in_map(right, region_base_map.len(), region_base_map.get(0).unwrap().len())
            && *region_base_map.get(right.y as usize).unwrap().get(right.x as usize).unwrap() == region_code {
            if !explored_coords.contains(&right) && !coords_to_explore.contains(&right) {
                coords_to_explore.push(right)
            }
        } else {
            exposed_sides.right = true;
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

fn count_exposed_sides(exposed_sides: &ExposedSides) -> i32 {
    let mut exposed: i32 = 0;
    if exposed_sides.up {
        exposed += 1;
    }
    if exposed_sides.down {
        exposed += 1;
    }
    if exposed_sides.left {
        exposed += 1;
    }
    if exposed_sides.right {
        exposed += 1;
    }
    exposed
}

fn calculate_bulk_walls(subregions: &Vec<SubRegion>) -> i32 {
    let mut bulk_perimeter = 0;

    let mut left_walls = vec![];
    let mut right_walls = vec![];
    let mut up_walls = vec![];
    let mut down_walls = vec![];

    for reg in subregions.iter() {
        if reg.exposed_sides.up {
            up_walls.push(reg);
        }
        if reg.exposed_sides.down {
            down_walls.push(reg);
        }
        if reg.exposed_sides.left {
            left_walls.push(reg);
        }
        if reg.exposed_sides.right {
            right_walls.push(reg);
        }
    }

    left_walls.sort_by(|a, b| {
        if a.coordinate.x != b.coordinate.x {
            a.coordinate.x.cmp(&b.coordinate.x)
        } else {
            a.coordinate.y.cmp(&b.coordinate.y)
        }
    });
    right_walls.sort_by(|a, b| {
        if a.coordinate.x != b.coordinate.x {
            a.coordinate.x.cmp(&b.coordinate.x)
        } else {
            a.coordinate.y.cmp(&b.coordinate.y)
        }
    });
    up_walls.sort_by(|a, b| {
        if a.coordinate.y != b.coordinate.y {
            a.coordinate.y.cmp(&b.coordinate.y)
        } else {
            a.coordinate.x.cmp(&b.coordinate.x)
        }
    });
    down_walls.sort_by(|a, b| {
        if a.coordinate.y != b.coordinate.y {
            a.coordinate.y.cmp(&b.coordinate.y)
        } else {
            a.coordinate.x.cmp(&b.coordinate.x)
        }
    });

    let mut expected_coord = Coordinate{x: -1, y: -1};
    for wall in left_walls.iter() {
        if wall.coordinate != expected_coord {
            bulk_perimeter += 1;
        }
        expected_coord = Coordinate{x: wall.coordinate.x, y: wall.coordinate.y + 1};
    }

    let mut expected_coord = Coordinate{x: -1, y: -1};
    for wall in right_walls.iter() {
        if wall.coordinate != expected_coord {
            bulk_perimeter += 1;
        }
        expected_coord = Coordinate{x: wall.coordinate.x, y: wall.coordinate.y + 1};
    }

    let mut expected_coord = Coordinate{x: -1, y: -1};
    for wall in up_walls.iter() {
        if wall.coordinate != expected_coord {
            bulk_perimeter += 1;
        }
        expected_coord = Coordinate{x: wall.coordinate.x + 1, y: wall.coordinate.y};
    }

    let mut expected_coord = Coordinate{x: -1, y: -1};
    for wall in down_walls.iter() {
        if wall.coordinate != expected_coord {
            bulk_perimeter += 1;
        }
        expected_coord = Coordinate{x: wall.coordinate.x + 1, y: wall.coordinate.y};
    }

    bulk_perimeter
}

fn calculate_region_cost(region_map: &RegionMap, bulk: bool) -> u64 {
    let area = region_map.sub_regions.len();
    let perimeter: i32;

    if bulk {
        perimeter = calculate_bulk_walls(&region_map.sub_regions);
    } else {
        perimeter = region_map.sub_regions.iter().fold(0, |a, b| a + count_exposed_sides(&b.exposed_sides));
    }

    log::debug!("Found region name {:?} with area of {:?} and perimeter of {:?}", region_map.region_code, area, perimeter);
    area as u64 * perimeter as u64
}

fn calculate_total_region_cost(region_map: &Vec<RegionMap>, bulk: bool) -> u64 {
    let mut total_cost = 0;
    for region in region_map.iter() {
        total_cost += calculate_region_cost(region, bulk);
    }
    total_cost
}

pub(crate) fn explore_regions(region_map: &Vec<Vec<char>>, bulk: bool) -> u64 {
    let mut discovered_regions: Vec<RegionMap> = vec![];

    for (y_idx, row) in region_map.iter().enumerate() {
        for (x_idx, _) in row.iter().enumerate() {
            let curr_coord = Coordinate{ x: x_idx as i32, y: y_idx as i32 };
            if !test_coord_in_regions(&discovered_regions, curr_coord) {
                discovered_regions.push(explore_region(region_map, curr_coord));
            }
        }
    }

    calculate_total_region_cost(&discovered_regions, bulk)
}