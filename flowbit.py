#!/usr/bin/env python3

import subprocess
import shutil
import sys
import getopt
import os


def main(argv):
    config_file_path = argv[2]

    subprocess.call(['cp', config_file_path, '.'])
    subprocess.call(['mvn', 'scala:run', '-DmainClass=main.scala.MainFromConfig'])

if __name__ == "__main__":
    main(sys.argv)

