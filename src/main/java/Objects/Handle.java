package Objects;

public class Handle {
    private String handle;

    public Handle(String id){handle = id;}

    public String getHandle() {return handle;}

    public void setHandle(String handle) {this.handle = handle;}

    @Override
    public boolean equals(Object o){
        if (!o.getClass().equals(Handle.class)) return false;
        return (((Handle) o).handle.equals(handle));
    }
}
