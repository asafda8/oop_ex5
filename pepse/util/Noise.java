package pepse.util;

public class Noise {
 /**
  * variables for perlin private usage
   */
private static final int P_NUM = 512;
 private static final int MIN_SIZE = 1;
 private static final int MIN_RANGE = 0;
 private static final int MAX_RANGE = 256;
 private static final int DEFAULT_SIZE = 35;
 private static final int COMPARE_BITS = 255;
 private static final float INIT_VALUE = 0.0f;
 private static final float FACTOR_SIZE = 2.0f;
 private static final int NEIGHBOR = 1;
 private static final int CHANGE_VAL = 1;
 private static final float FADE_VAL_DUP = 6;
 private static final float FADE_VAL_SUB = 15;
 private static final float FADE_VAL_ADD = 10;
private static final int BITS_TO_CONVERT = 15;
 private static final int GRADIENT_MAX = 8;
 private static final int GRADIENT_FOUR = 4;
 private static final int GRADIENT_TWELVE = 12;
 private static final int GRADIENT_FOURTEEN = 14;
 private static final int BITS_ZERO = 0;
 private static final int BITS_ONE = 1;
 private static final int BITS_TWO = 2;

 private static final int[] PERMUTATION = new int[]{151, 160, 137, 91, 90, 15, 131, 13, 201,
             95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99,
             37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26,
             197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88,
             237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74,
             165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111,
             229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40,
             244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76,
             132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159,
             86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250,
             124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207,
             206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170,
             213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155,
             167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113,
             224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242,
             193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235,
             249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184,
             84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236,
             205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66,
             215, 61, 156, 180};


         /**
  * Perlin Noise implementation.
 */
  private final float seed;
 private long default_size;
 private int[] p;

         /**
  * Constructor
  *
  * @param seed Seed to pseudo-randomize the input.

  */
        public Noise(float seed) {

         this.seed = seed;
         init();
         }


         /**
  * Initializes the required class functionality.
  */
         private void init() {
        this.p = new int[P_NUM];
         this.default_size = DEFAULT_SIZE;
         for (int i = MIN_RANGE; i < MAX_RANGE; i++) {
             p[MAX_RANGE + i] = p[i] = PERMUTATION[i];
             }
         }

         /**
  * 1D noise generation
  *
  * @param x X value. Float.
  * @return Noise per given value.
  */
         public float noise(float x) {
         float value = INIT_VALUE;
         float size = default_size;
         float initialSize = size;

         while (size >= MIN_SIZE) {
             value += smoothNoise((x / size), (INIT_VALUE / size), (INIT_VALUE / size)) * size;
             size /= FACTOR_SIZE;
             }
         return value / initialSize;
         }

         /**
  * Generates 3D-based smooth noise.
  *
  * @param x X axis.
  * @param y Y axis.
  * @param z Z axis.
  * @return Smooth noise.
  */
         public float smoothNoise(float x, float y, float z) {
         // Offset each coordinate by the seed value
         x += this.seed;
         y += this.seed;
         x += this.seed;

         int X = (int) Math.floor(x) & COMPARE_BITS; // FIND UNIT CUBE THAT
         int Y = (int) Math.floor(y) & COMPARE_BITS; // CONTAINS POINT.
         int Z = (int) Math.floor(z) & COMPARE_BITS;

         x -= Math.floor(x); // FIND RELATIVE X,Y,Z
         y -= Math.floor(y); // OF POINT IN CUBE.
         z -= Math.floor(z);

        float u = fade(x); // COMPUTE FADE CURVES
        float v = fade(y); // FOR EACH OF X,Y,Z.
        float w = fade(z);
         int A = p[X] + Y;
         int AA = p[A] + Z;
         int AB = p[A + NEIGHBOR] + Z; // HASH COORDINATES OF
         int B = p[X + NEIGHBOR] + Y;
         int BA = p[B] + Z;

         int BB = p[B + NEIGHBOR] + Z; // THE 8 CUBE CORNERS,

         return lerp(w, lerp(v, lerp(u, grad(p[AA], x, y, z), // AND ADD
                                 grad(p[BA], x - CHANGE_VAL, y, z)), // BLENDED
                         lerp(u, grad(p[AB], x, y - CHANGE_VAL, z), // RESULTS
                                 grad(p[BB], x - CHANGE_VAL, y - CHANGE_VAL, z))),// FROM 8
                lerp(v, lerp(u, grad(p[AA + NEIGHBOR], x, y, z - CHANGE_VAL), // CORNERS
                               grad(p[BA + NEIGHBOR], x - CHANGE_VAL, y, z - CHANGE_VAL)), // OF CUBE
                         lerp(u, grad(p[AB + NEIGHBOR], x, y - CHANGE_VAL, z - CHANGE_VAL),
                                 grad(p[BB + NEIGHBOR], x - CHANGE_VAL, y - CHANGE_VAL, z - CHANGE_VAL))));
         }
         /**
 * Utility method for the noise generation.
  *
  * @param t Param.
  * @return Fade value.
  */
         private float fade(float t) {
         return t * t * t * (t * (t * FADE_VAL_DUP - FADE_VAL_SUB) + FADE_VAL_ADD);
         }

         /**
  * Linear interpolation.
  *
  * @param t Interpolation parameter.
  * @param a Value 1.
  * @param b Value 2.
  * @return Linear interpolation based on the value.
  */
         private float lerp(float t, float a, float b) {
         return a + t * (b - a);
         }

         /**
  * Hashed gradient.
  *
  * @param hash Linear interpolation value converted to hash code.
  * @param x X axis.
  * @param y Y axis.
  * @param z Z axis.
  * @return Hashed gradient value.
  */
         private float grad(int hash, float x, float y, float z) {
         int h = hash & BITS_TO_CONVERT; // CONVERT LO 4 BITS OF HASH CODE
         float u = h < GRADIENT_MAX ? x : y, // INTO 12 GRADIENT DIRECTIONS.
         v = h < GRADIENT_FOUR ? y : h == GRADIENT_TWELVE || h == GRADIENT_FOURTEEN ? x : z;
         return ((h & BITS_ONE) == BITS_ZERO ? u : -u) + ((h & BITS_TWO) == BITS_ZERO ? v : -v);
         }
 }