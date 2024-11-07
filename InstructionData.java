
public  class InstructionData {
  public int op_6;
  public int op_8;
  public int op_10;
  public int op_11;
  public int rn; 
  public int rm;
  public int rd;
  public int shamt;
  public int aluImmediate;
  public int DT_address;
  public int BR_address;
  public int C_BR_address;


  public InstructionData(int instruction) {
    op_6 = instruction >> 26 & 0x3F;
    op_8 = instruction >> 24 & 0xFF;
    op_10 = instruction >> 22 & 0x3FF;
    op_11 = instruction >> 21 & 0x7FF;
    rn = instruction >> 5 & 0x1F;
    rm = instruction >> 16 & 0x1F;
    rd = instruction & 0x1F;
    shamt = instruction >> 10 & 0x3F;
    aluImmediate = instruction >> 10 & 0xFFF;
    DT_address = instruction >> 12 & 0x1FF;
    BR_address = instruction & 0x3FFFFFF;
    C_BR_address = instruction >> 5 & 0x7FFFF;
  }
}

