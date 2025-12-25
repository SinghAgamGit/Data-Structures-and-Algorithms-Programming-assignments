class EdgeB{
    private int u;
    private int v; 
    private int cost; 

    EdgeB(){
      this(0, 0, 0);
    }

    EdgeB(int Station1index, int Station2index, int c){
      u = Station1index;
      v = Station2index;
      cost = c;
    }

    public int getCost(){
      return cost;
    }
    public int getu(){
      return u;
    }
    public int getv(){
      return v;
    }
}