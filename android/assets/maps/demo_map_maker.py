#!/usr/bin/env python3


import traceback
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
        if type == 1 or type == 2 or type == 4:
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

def get_pos(count):
    question = "x y"
    if count > 1:
        for i in range(2, count):
            question += "x{} y{}".format(i,i)
    pos = input("Select position ({}): ".format(question))
    pos = pos.split()
    pos = map(lambda x: int(x), pos)
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
    print("\t Quit [q]")
    while True:
        action = input("Choose your action :")
        if action == "q"  or action == "s" or int(action) > 0:
            return action
        else:
            print("Invalid selection")

if __name__ == "__main__":
    size_x = int(input("Enter Width: "))
    size_y = int(input("Enter Height: "))

    map = Map(size_x, size_y)
    while True:
        try:
            action = show_menu()
            if action == "q":
                break
            if action == "s":
                map.save()
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
                map.set_block_type(type)
            if action == "2":
                map.set_block(*get_pos(2))
            if action == "3":
                map.set_block_movement_end(*get_pos(4))
            if action == "4":
                map.fill()
            if action == "5":
                map.del_block(*get_pos(2))
            if action == "6":
                type = int(input("Select key type(None[0]/Gold[1]/Pink[2]/Green[4]): "))
                map.set_key(*get_pos(2),type)
            if action == "7":
                trigger = input("Enter trigger text: ")
                map.set_trigger(*get_pos(2),trigger)
            if action == "8":
                map.set_start_end(*get_pos(4))
            if action == "9":
                map.show()

        except IndexError as e:
            print(traceback.format_exception(None,  # <- type(e) by docs, but ignored
                                             e, e.__traceback__),
                  file=sys.stderr, flush=True)
            print()
    map.save()