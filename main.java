import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Main {
  public static void main(String[] args) throws IOException {
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


    System.out.println(Integer.toBinaryString(instructions[0]));
    
  }
}

// 1001 0001 0010 0000 0010 0011 1110 0000