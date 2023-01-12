## Redstone MCU design document

Redstone MCU is a user-programmable 8-bit microcontroller unit with 128 bytes of built-in memory and 32-byte internal stack. The MCU is capable of receiving and outputting redstone signals from its 4 peripheral ports depending on user-defined configuration.

### Physical Design
- 4 redstone signal ports (North, East, South, West).
- Half-slab. Placeable on any solid block.

### Registers
**A register**  
Accumulator. Target of arithmetic operations, general-purpose otherwise. Directly addresable by user code. This is the only register able to interface with internal memory.

**B register**  
Extra register completely dedicated towards general-purpose usage. Directly addressable by user code.

**C register**  
Status code register. In case of a fault, the MCU sets this register to an appropriate status code. Non-addressable by user code. The status codes are documented in the "Microcontroller Fault Codes" section.

**S register**  
Stack pointer. Non-addressable by user code. Can be indirectly modified by `PUSH`, `POP`, `CALL` and `RET` instructions.

**F register**  
Flags register. Modified by different instructions to signal different outcomes of various operations. Non-addressable by user code. The F register is documented in the "Microcontroller Operation Flags" section.

**NS, EW registers**  
Pseudo-registers mapped to 4 of the available redstone ports. Both are directly addressable from user-code. The inputs are combined into 2 registers divided as follows. 
```
  NS register        EW register
  +----+----+        +----+----+
N |3210|3210| S    E |3210|3210| W
  +----+----+        +----+----+
   7654 3210          7654 3210
````

**P register**  
Port configuration register. Divided into 4 nibbles, each can be set to one of three different states defining a port's behavior. The details on redstone signal port configuration are documented in the "Redstone Port Configuration" section.

