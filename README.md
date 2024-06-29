# AsconHash Java Implementation

This project is a Java implementation of the AsconHash algorithm. Ascon is a lightweight and secure cryptographic hash function. This implementation allows you to hash input data and get the hash output.

## Prerequisites

You need to have Java installed on your machine to run this project.

## Usage

The main class is `Main.java` which takes a hexadecimal string as input from the console. The input is then converted to a byte array and passed to the `hash` method of the `AsconHash` class. The `hash` method returns a byte array which is the hash of the input data. The byte array is then converted back to a hexadecimal string and printed to the console.

Here is an example of how to use it:

1. Run the `Main.java` class.
2. Enter your hexadecimal string when prompted.

The output will be the Ascon hash of the input data.

## Tests

Test vectors in the `tests` folder are generated from the `crypto_hash/asconhashv12` of the [original C implementation](https://github.com/ascon/ascon-c).

You can use the `run.sh` script to run the tests. The script compiles the project and runs the tests. The output of the tests will be printed to the console.

## License

This project is licensed under the MIT License.