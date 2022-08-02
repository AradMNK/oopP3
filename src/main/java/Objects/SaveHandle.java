package Objects;

public class SaveHandle{
    private int handle;

    public SaveHandle(int id){handle = id;}

    public int getHandle() {return handle;}

    public void setHandle(int handle) {this.handle = handle;}

    @Override
    public boolean equals(Object o){
        if (!o.getClass().equals(SaveHandle.class)) return false;
        return (((SaveHandle) o).handle == handle);
    }

    @Override
    public String toString(){return Integer.toString(handle);}
}
