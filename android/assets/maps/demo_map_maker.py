#!/usr/bin/env python3

import sys
import json

def make_file(size_x, size_y, filename, type, type1):
    data = {
        "start_x":0,
        "start_y":0,
        "end_x":10,
        "end_y":10,
        "tiles" : [None]*(size_x*size_y)
        };
    for x in range(size_x):
        for y in range(size_y):
            i = x*size_y+y
            data["tiles"][i] = {"x":x, "y":y, "type": type1 if x == y else type }

    with open(filename, "w+") as f:
        json.dump(data, f, indent=4);

if __name__ == "__main__":
    size_x = 10
    size_y = 10
    filename = "01.json"
    type = "Stone"
    type1 = "Ice"
    if len(sys.argv) >= 4:
        size_x = int(sys.argv[1])
        size_y = int(sys.argv[2])
        filename = sys.argv[3]
    if len(sys.argv) >= 6:
        type = sys.argv[4]
        type1 = sys.argv[5]
    make_file(size_x, size_y, filename, type, type1)
        