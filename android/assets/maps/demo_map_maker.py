#!/usr/bin/env python3


import traceback
import sys
import os
import json
from pprint import pprint

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

    def load_file(self, filename):
        data = ""
        with open(filename, "r") as f:
            data = json.load(f)

        self.__init__(data["width"], data["height"])
        self.start = (data["start_x"], data["start_y"])
        self.end = (data["end_x"], data["end_y"])
        for tile in data["tiles"]:
            i = tile["x"]*self.height+tile["y"]
            self.tiles[i] = tile
        print("completed import of ", filename)

    def set_start_end(self, startx, starty, endx, endy):
        self.start = (startx, starty)
        self.end = (endx, endy)

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

                if self.tiles[i]["key"] > 0:
                    print("k", end="")
                else:
                    print(" ", end="")

            print()

    def set_block_type(self, type):
        self.type = type

    def set_block(self, posx, posy):
        i = posx*self.height+posy
        self.tiles[i]["type"] = self.type
        print("Set block at ({}, {}) to {}".format(posx, posy, self.type))

    def set_block_movement_end(self, posx, posy, endx, endy):
        i = posx*self.height+posy
        self.tiles[i]["x2"] = endx
        self.tiles[i]["y2"] = endy

        print("Set block move from ({}, {}) to ({},{})".format(posx, posy, endx, endy))

    def set_trigger(self, posx, posy, trigger):
        i = posx*self.height+posy
        self.tiles[i]["trigger"] = trigger

        print("Set trigger for block ({}, {})".format(posx, posy))

    def set_key(self, posx, posy, type):
        i = posx * self.height + posy
        if type in [0, 1, 2, 4]:
            self.tiles[i]["key"] = type
            print("Set Key at ({}, {}) to {}".format(posx, posy, ["None","Gold","Pink","Dummy","Green"][type]))

    def fill(self):
        for x in range(self.width):
            for y in range(self.height):
                i = x * self.height + y
                self.tiles[i] = {
                    "x": x,
                    "y": y,
                    "type": self.type,
                    "key": 0
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

_map = None

def get_pos(count):
    question = "x y"
    if count > 2:
        for i in range(1, int(count/2)):
            question += " x{} y{}".format(i,i)
    pos = input("Select position ({}): ".format(question))
    pos = pos.split()
    pos = list(map(lambda x: int(x), pos))
    return pos[:count]


def show_menu():
    print("Possible actions:")
    print("\t Set block type [1]")
    print("\t Draw block [2]")
    print("\t Draw block movement [3]")
    print("\t Fill [4]")
    print("\t Delete block [5]")
    print("\t Set key [6]")
    print("\t Set trigger [7]")
    print("\t Set start/exit [8]")
    print("\t Show [9]")
    print("\t Save as [s]")
    print("\t Load from [l]")
    print("\t Quit [q]")
    while True:
        action = input("Choose your action :")
        if action in "qsl" or int(action) > 0:
            return action
        else:
            print("Invalid selection")

def show_draw_block_menu():
    global _map
    while True:
        _map.show()
        pos = input("Select position (x y): ")
        os.system("clear")
        pos = pos.split()
        if len(pos) > 0 and pos[0] == "q":
            return
        elif len(pos) == 2:
            _map.set_block(int(pos[0]), int(pos[1]))
        else:
            print("Invalid input. Retry")


def main():
    size_x = int(input("Enter Width: "))
    size_y = int(input("Enter Height: "))
    global _map
    _map = Map(size_x, size_y)


    while True:
        try:
            action = show_menu()
            os.system("clear")
            if action == "q":
                break
            if action == "s":
                _map.save()
            if action == "l":
                fname = input("File to load: ")
                _map.load_file(fname)
            if action == "1":
                print("block types to choose from:")
                print("\tWall")
                print("\tStone")
                print("\tIce")
                print("\tHPlank")
                print("\tVPlank")
                print("\tMetal")
                print("\tEmpty")
                type = input("Select block type: ")
                _map.set_block_type(type)
            if action == "2":
                show_draw_block_menu()
            if action == "3":
                _map.set_block_movement_end(*get_pos(4))
            if action == "4":
                _map.fill()
            if action == "5":
                _map.del_block(*get_pos(2))
            if action == "6":
                type = int(input("Select key type(None[0]/Gold[1]/Pink[2]/Green[4]): "))
                _map.set_key(*get_pos(2),type)
            if action == "7":
                trigger = input("Enter trigger text: ")
                _map.set_trigger(*get_pos(2),trigger)
            if action == "8":
                _map.set_start_end(*get_pos(4))
            if action == "9":
                _map.show()

        except Exception as e:
            print(traceback.format_exception(None,  # <- type(e) by docs, but ignored
                                             e, e.__traceback__),
                  file=sys.stderr, flush=True)
            print()

    _map.save()

if __name__ == "__main__":
    main()