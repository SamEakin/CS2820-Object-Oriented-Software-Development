class X {                         // a
        private int i;            // b
        //private X(){}             // c
        static X factory(int v){  // d
                X r = new X();    // e
                r.i = v;          // f
                return r;         // g
        }                         // h
		public static void main(String[] args) {
			X thing = factory(10);		
		}
}                                 


