#!/usr/bin/env python3

import json

data = {
    "start_x":0,
    "start_y":0,
    "end_x":10,
    "end_y":10,
    "tiles" : [None]*100
    };
for x in range(10):
    for y in range(10):
        i = x*10+y
        data["tiles"][i] = {"x":x, "y":y, "type": "Ice" if x == y else "Stone" }

with open("01.json", "w+") as f:
    json.dump(data, f, indent=4);
