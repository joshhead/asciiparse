# asciiparse

Parse a particular format of ascii-art numbers

## Installation and Usage

First, make sure [Leiningen](http://leiningen.org/) is installed.
Then you can run the parser with `lein run`.
The first argument should be the name of an input file in the correct format.
The second argument may be an output file to write the formatted data to.
If no output file is specified, the formatted data will be written to standard out.

The testdata folder contains some example inputs.

    git clone https://github.com/joshhead/asciiparse.git
    cd asciiparse
    lein run testdata/wellformed.txt


## Examples

Input file testdata/illegible.txt

        _  _     _  _  _  _  _ 
      | _| _||_| _ |_   ||_||_|
      ||_  _|  | _||_|  ||_| _ 
                               
                               
      |  |  |  |  |  |  |  |  |
      |  |  |  |  |  |  |  |  |
                               
     _  _  _  _  _  _  _  _    
    | || || || || || || ||_   |
    |_||_||_||_||_||_||_| _|  |
                               
        _  _  _  _  _  _     _ 
    |_||_|| || ||_   |  |  | _ 
      | _||_||_||_|  |  |  | _|
                               
        _  _     _  _  _  _  _ 
      | _| _||_| _ |_   ||_||_|
      ||_  _|  | _||_|  ||_| _ 
                               

Printing to terminal:

    $ lein run testdata/illegible.txt
    1234?678? ILL
    111111111 ERR
    000000051
    49006771? ILL
    1234?678? ILL

Writing to file:

    $ lein run testdata/illegible.txt out.txt

out.txt:

    1234?678? ILL
    111111111 ERR
    000000051
    49006771? ILL
    1234?678? ILL


### Bugs

The single argument version does not print to stdout when running from an uberjar.

    lein uberjar
    java -jar target/asciiparse-*-standalone.jar testdata/wellformed.txt

## License

Copyright © 2014 Josh Headapohl

MIT License
