


class Voxel{
	private Voxel[] daughter;
	private int depth;
	private Material mat;
	
	private Vector cord;
	private int size;
	
	
	
	Voxel(Vector c, int s, Material m, int d){
		this.cord = c;
		this.size = s;
		this.mat = m;
		this.depth = d;
		
		this.daughter = new Voxel[8];
		for(int i = 0; i < 8; i++){
			this.daughter[i] = new Voxel();// must be setted in future
		}
	}
	
	public void setMaterial(Material x){this.mat = x;}
	//function of get Intersection with this voxel object. Can be optimoze in future
	public double getIntersection(Vector camera, Vector alpha){
		
	}
}