import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Main {
  public static Map<Integer, String> makeConditionMap() {
    HashMap<Integer, String> conditionMap = new HashMap<>();

    // Add the values to the hashmap
    conditionMap.put(0x0, "EQ");
    conditionMap.put(0x1, "NE");
    conditionMap.put(0x2, "HS");
    conditionMap.put(0x3, "LO");
    conditionMap.put(0x4, "MI");
    conditionMap.put(0x5, "PL");
    conditionMap.put(0x6, "VS");
    conditionMap.put(0x7, "VC");
    conditionMap.put(0x8, "HI");
    conditionMap.put(0x9, "LS");
    conditionMap.put(0xa, "GE");
    conditionMap.put(0xb, "LT");
    conditionMap.put(0xc, "GT");
    conditionMap.put(0xd, "LE"); 

    return conditionMap;
  }  

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
    instructionMap.put(0b01010100, "B.");
    return instructionMap;
  }


  public static void main(String[] args) throws IOException {
    Map<Integer, String> instructionMap = makeInstructionMap();
    Map<Integer, String> conditionMap = makeConditionMap();
    ArrayList<String> strToPrint = new ArrayList<>();

    // Key = line number, Value = label number
    Map<Integer, Integer> labelMap = new HashMap<>();
    int labelCount = 0;


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
        String instName = instructionMap.get(instruction.op_6);
        int convertedBRAddress = convertTo2s(instruction.BR_address, 26);
        if(!labelMap.containsKey(i+convertedBRAddress)) {
          labelCount++;
          labelMap.put(i+convertedBRAddress, labelCount );
        }
        strToPrint.add(instName + " label_" + labelMap.get(i+convertedBRAddress ));
      }

      else if (instructionMap.containsKey(instruction.op_8)) {
        // CB Type
        
        String instName;
        int convertedC_BR_Address = convertTo2s(instruction.C_BR_address, 19);
        if(instruction.op_8 == 0b01010100) {
          instName = ("B."+conditionMap.get(instruction.rd));
        }
        else {
          instName = instructionMap.get(instruction.op_8);
        }
        if(!labelMap.containsKey(i+convertedC_BR_Address)) {
          labelCount++;
          labelMap.put(i+convertedC_BR_Address, labelCount );
          // strToPrint.add(i+instruction.C_BR_address, "label_" + labelCount + ":");
        }
        strToPrint.add(instName + " label_" + labelMap.get(i+convertedC_BR_Address ));
      }
      else if (instructionMap.containsKey(instruction.op_10)) {
        String instructionString = instructionMap.get(instruction.op_10);
        //I Type
        strToPrint.add(instructionString + " X" + instruction.rd + ", X" + instruction.rn + ", #" + convertTo2s(instruction.aluImmediate,12));
      }
      else if (instructionMap.containsKey(instruction.op_11)) {
        String instructionString = instructionMap.get(instruction.op_11);
        if(instructionString.charAt(instructionString.length() - 1) == ':') {
          // D type
          instructionString = instructionString.substring(0, instructionString.length() - 1);
          strToPrint.add(instructionString + " X" + instruction.rd + ", [X" + instruction.rn + ", #" + instruction.DT_address + "]");
        }
        else {
          if("LSL".equals(instructionString) || "LSR".equals(instructionString)) {
            // R type (Shift)
            strToPrint.add(instructionString + " X" + instruction.rd + ", X" + instruction.rn + ", #" + instruction.shamt);
          }
          else {
            // R type
            strToPrint.add(instructionString + " X" + instruction.rd + ", X" + instruction.rn + ", X" + instruction.rm);
          }
          
        }

      }
    }
    
    List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(labelMap.entrySet());
    entryList.sort(Map.Entry.comparingByKey());
    
    int count = 0;
    for (Map.Entry<Integer, Integer> label : entryList) {
        if (label.getKey() + count < strToPrint.size()) {
            strToPrint.add(label.getKey() + count, "label_" + label.getValue() + ":");
        } else {
            strToPrint.add("label_" + label.getValue() + ":");
        }
        count++;
    }
    
    for (int i = 0; i < strToPrint.size(); i++) {
        System.out.println(strToPrint.get(i));
    }
  }
  
  public static int convertTo2s(int num, int bits) {
    int mask = (1 << bits) - 1; 
    int maskedNum = num & mask;

    int signBit = 1 << (bits - 1);
    if ((maskedNum & signBit) != 0) {
        maskedNum -= (1 << bits);
    }

    return maskedNum;
}

}



// 1001 0001 0010 0000 0010 0011 1110 0000
// 00010111111111111111111111111111