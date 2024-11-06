import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

class Main {
  
  public static Map<Integer, String> makeInstructionMap() {
    Map<Integer, String> instructionMap = new HashMap<>();

    instructionMap.put(0b10001011000, "ADD");
    instructionMap.put(0b1001000100, "ADDI");
    instructionMap.put(0b1011000100, "ADDIS:");
    instructionMap.put(0b10101011000, "ADDS");
    instructionMap.put(0b10001010000, "AND");
    instructionMap.put(0b1001001000, "ANDI");
    instructionMap.put(0b1111001000, "ANDIS:");
    instructionMap.put(0b1110101000, "ANDS");
    instructionMap.put(0b000101, "B");
    instructionMap.put(0b100101, "BL");
    instructionMap.put(0b11010110000, "BR");
    instructionMap.put(0b10110101, "CBNZ");
    instructionMap.put(0b10110100, "CBZ");
    instructionMap.put(0b11111111110, "DUMP");
    instructionMap.put(0b11001010000, "EOR");
    instructionMap.put(0b1101001000, "EORI");
    instructionMap.put(0b00011110011, "FADDD");
    instructionMap.put(0b00011110001, "FADDS");
    instructionMap.put(0b00011110011, "FCMPD");
    instructionMap.put(0b00011110001, "FCMPS");
    instructionMap.put(0b00011110011, "FDIVD");
    instructionMap.put(0b00011110001, "FDIVS");
    instructionMap.put(0b00011110011, "FMULD");
    instructionMap.put(0b00011110001, "FMULS");
    instructionMap.put(0b00011110011, "FSUBD");
    instructionMap.put(0b00011110001, "FSUBS");
    instructionMap.put(0b11111111111, "HALT");
    instructionMap.put(0b11111000010, "LDUR:");
    instructionMap.put(0b00111000010, "LDURB:");
    instructionMap.put(0b11111100010, "LDURD:");
    instructionMap.put(0b01111000010, "LDURH:");
    instructionMap.put(0b10111100010, "LDURS:");
    instructionMap.put(0b10111000100, "LDURSW:");
    instructionMap.put(0b11010011011, "LSL");
    instructionMap.put(0b11010011010, "LSR");
    instructionMap.put(0b10011011000, "MUL");
    instructionMap.put(0b10101010000, "ORR");
    instructionMap.put(0b1011001000, "ORRI");
    instructionMap.put(0b11111111100, "PRNL");
    instructionMap.put(0b11111111101, "PRNT");
    instructionMap.put(0b10011010110, "SDIV");
    instructionMap.put(0b10011011010, "SMULH");
    instructionMap.put(0b11111000000, "STUR:");
    instructionMap.put(0b00111000000, "STURB:");
    instructionMap.put(0b11111100000, "STURD:");
    instructionMap.put(0b01111000000, "STURH:");
    instructionMap.put(0b10111100000, "STURS:");
    instructionMap.put(0b10111000000, "STURSW:");
    instructionMap.put(0b11001011000, "SUB");
    instructionMap.put(0b1101000100, "SUBI");
    instructionMap.put(0b1111000100, "SUBIS");
    instructionMap.put(0b11101011000, "SUBS");
    instructionMap.put(0b10011010110, "UDIV");
    instructionMap.put(0b10011011110, "UMULH");
    return instructionMap;
  }


  public static void main(String[] args) throws IOException {
    Map<Integer, String> instructionMap = makeInstructionMap();

    Path path = Paths.get("./assignment1.legv8asm.machine");
    byte[] fileContents = Files.readAllBytes(path);
    int[] instructions = new int[fileContents.length / 4];

    for (int i = 0; i < fileContents.length; i += 4) {
      // Combine four bytes to form a 32-bit instruction
      instructions[i / 4] = ((fileContents[i]) << 24 & 0xFF000000) |
                            ((fileContents[i + 1] ) << 16 & 0xFF0000) |
                            ((fileContents[i + 2] ) << 8 & 0xFF00) |
                            (fileContents[i + 3] & 0xFF);
    }

    for(int i = 0; i < instructions.length; i++) {

      InstructionData instruction = new InstructionData(instructions[i]);
      if(instructionMap.containsKey(instruction.op_6)) {
        // B Type
        System.out.println("B");
      }
      else if (instructionMap.containsKey(instruction.op_8)) {
        // CB Type
        System.out.println("CB");
      }
      else if (instructionMap.containsKey(instruction.op_10)) {
        String instructionString = instructionMap.get(instruction.op_10);
        //I Type
        System.out.println(instructionString + " X" + instruction.rd + ", X" + instruction.rn + ", #" + instruction.aluImmediate);
      }
      else if (instructionMap.containsKey(instruction.op_11)) {
        String instructionString = instructionMap.get(instruction.op_11);
        if(instructionString.charAt(instructionString.length() - 1) == ':') {
          instructionString = instructionString.substring(0, instructionString.length() - 1);
          System.out.println(instructionString + " X" + instruction.rd + " [X" + instruction.rn + ", #" + instruction.DT_address + "]");

        }
        else {
          if("LSL".equals(instructionString) || "LSR".equals(instructionString)) {

            System.out.println(instructionString + " X" + instruction.rd + ", X" + instruction.rn + ", #" + instruction.shamt);
          }
          else {

            System.out.println(instructionString + " X" + instruction.rd + ", X" + instruction.rn + ", X" + instruction.rn);
          }
          
        }

      }
    }
    InstructionData test = new InstructionData(instructions[0]);
    System.out.println(Integer.toBinaryString(test.op_6));
    System.out.println(Integer.toBinaryString(test.op_8));
    System.out.println(Integer.toBinaryString(test.op_10));
    System.out.println(Integer.toBinaryString(test.op_11));
    System.err.println(Integer.toBinaryString(test.rn));
    System.err.println(Integer.toBinaryString(test.rm));
    System.err.println(Integer.toBinaryString(test.rd));
    System.err.println(Integer.toBinaryString(test.shamt));
  }
}

// 1001 0001 0010 0000 0010 0011 1110 0000