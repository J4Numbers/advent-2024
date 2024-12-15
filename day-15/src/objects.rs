#[derive(Copy, Clone, Hash, Eq, PartialEq, Debug)]
pub struct NumberPair {
    pub x: i32,
    pub y: i32,
}

#[derive(Copy, Clone, Hash, Eq, PartialEq, Debug)]
pub struct Box {
    pub left_loc: NumberPair,
    pub moved: bool,
    pub width: usize,
}

#[derive(Clone, Hash, Eq, PartialEq, Debug)]
pub struct FileReport {
    pub grid: Vec<Vec<char>>,
    pub box_locs: Vec<Box>,
    pub robot_loc: NumberPair,
    pub instructions: Vec<NumberPair>,
}
