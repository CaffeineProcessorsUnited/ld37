#!/usr/bin/env python3

import sys
import json

class Map:
    def __init__(self, width, height):
        self.width = width
        self.height = height
        self.start = (0,0)
        self.end = (0,0)
        self.type = None
        self.tiles = [None]*(width*height)
        self.filename = None
        self.fill()

    def show(self):
        print("{:^3}".format(""),end="")
        for x in range(self.width):
            print("{:^3}".format(x),end="")
        print()
        for y in reversed(range(self.height)):
            print("{:^3}".format(y), end="")
            for x in range(self.width):
                i = x*self.height+y
                if self.tiles[i]["type"]:
                    print("{:>2}".format(self.tiles[i]["type"][0]),end="")
                else:
                    print("{:>2}".format("E"),end="")
                print("k" if self.tiles[i]["key"] else " ", end="")

            print()

    def set_block_type(self, type):
        self.type = type

    def set_block(self, posx, posy):
        i = posx*self.height+posy
        self.tiles[i]["type"] = self.type
        print("Set block at ({}, {}) to {}".format(posx, posy, self.type))

    def toggle_key(self, posx, posy):
        i = posx * self.height + posy
        self.tiles[i]["key"] = not self.tiles[i]["key"]
        print("Set Key at ({}, {}) to {}".format(posx, posy, self.tiles[i]["key"]))

    def fill(self):
        for x in range(self.width):
            for y in range(self.height):
                i = x * self.height + y
                self.tiles[i] = {
                    "x": x,
                    "y": y,
                    "type": self.type,
                    "key": False
                }

    def del_block(self, posx, posy):
        i = posx*self.height+posy
        self.tiles[i]["type"] = None
        print("Remove block at ({}, {})".format(posx, posy))

    def save(self):
        if not self.filename:
            self.filename = input("Save file as: ")
            self.save()
        else:
            def f(data):
                return data["type"] is not None
            data = {
                "start_x":self.start[0],
                "start_y":self.start[1],
                "end_x":self.end[0],
                "end_y":self.end[1],
                "width": self.width,
                "height": self.height,
                "tiles": list(filter(f, self.tiles))
            }
            with open(self.filename, "w+") as f:
                json.dump(data, f, indent=4, sort_keys=True)

def show_menu():
    print("Possible actions:")
    print("\t Set block type [1]")
    print("\t Draw block [2]")
    print("\t Fill [3]")
    print("\t Delete block [4]")
    print("\t Set Key [5]")
    print("\t Show [6]")
    print("\t Save as [7]")
    print("\t Quit [q]")
    while True:
        action = input("Choose your action :")
        if action == "1" \
                or action == "2" \
                or action == "3" \
                or action == "4" \
                or action == "5" \
                or action == "6" \
                or action == "7" \
                or action == "q":
            return action
        else:
            print("Invalid selection")

if __name__ == "__main__":
    size_x = int(input("Enter Width: "))
    size_y = int(input("Enter Height: "))

    map = Map(size_x, size_y)
    while True:
        action = show_menu()
        if action == "q":
            break;
        if action == "1":
            type = input("Select block type: ")
            map.set_block_type(type)
        if action == "2":
            pos = input("Select position (x y): ")
            pos = pos.split()
            map.set_block(int(pos[0]), int(pos[1]))
        if action == "3":
            map.fill()
        if action == "4":
            pos = input("Select position (x y): ")
            pos = pos.split()
            map.del_block(int(pos[0]), int(pos[1]))
        if action == "5":
            pos = input("Select position (x y): ")
            pos = pos.split()
            map.toggle_key(int(pos[0]), int(pos[1]))
        if action == "6":
            map.show()
        if action == "7":
            map.save()
    map.save()