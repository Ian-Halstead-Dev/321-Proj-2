#!/bin/bash

# Check if a parameter (LEGv8 binary file) was passed
if [ -z "$1" ]; then
    echo "Usage: sh run.sh <legv8_binary_file>"
    exit 1
fi

# Run the disassembler or emulator on the provided file
#!/bin/bash

# Check if a parameter (LEGv8 binary file) was passed
if [ -z "$1" ]; then
    echo "Usage: sh run.sh assignment1.legv8asm.machine"
    exit 1
fi

# Run the emulator in binary emulation mode
/share/cs321/legv8emul $1 -b
