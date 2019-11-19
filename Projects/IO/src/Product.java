public class Product {

    private String name;
    private int count;
    private Double[] range;
    private Double min_rate;
    private String link;

    public Product(String name, int count, Double[] range, Double min_rate)
    {
        this.name = name;
        this.count = count;
        this.range = range;
        this.min_rate = min_rate;

    }

    public Product(){}

    public String Get_Name(){ return name; }

    public int Get_Count(){
        return count;
    }

    public Double[] get_Range(){return range;}

    public Double Get_Min_Rate(){return min_rate;}

    public String Get_Link(){return link;}

    public void Set_link(String link){this.link = link;}

    public void Set_Min_Rate(Double min_rate){this.min_rate = min_rate;}

    public void Set_Range(Double[] range){this.range=range;}

    public void Set_Name(String name){
        this.name = name;
    }

    public void Set_Count(int count){
        this.count = count;
    }


    public static void main(String args[]){

    }
}
