


class Voxel{
	private Voxel[] daughter;
	private int depth;
	private Material mat;
	
	public Vector cord;
	public int size;
	
	
	
	Voxel(Vector c, int s, Material m, int d){
		this.cord = c;
		this.size = s;
		this.mat = m;
		this.depth = d;
		
		this.daughter = new Voxel[8];
		for(int i = 0; i < 8; i++){
			Vector c_1 = new Vector(i/2%2,i%2,i/4);
			c_1 = Vector.multiply(c_1, s/2);
			this.daughter[i] = new Voxel(c_1 ,s/2 ,m,d+1);// must be setted in future
		}
	}
	
	public void setMaterial(Material x){this.mat = x;}
	
	
	public double renderVoxel(Vector camera, Vector alpha, int max_depth){
		if(max_depth == this.depth){
			return getIntersection(camera, alpha, this.cord, this.size);
		}
		double t = 0;
		for(int i = 0; i< 8; i++){
			t = getIntersection(camera, alpha, daughter[i].cord, daughter[i].size);
			if(t>=0) daughter[i].renderVoxel(camera, alpha, max_depth);
		}
	}
	
	//function of get Intersection with this voxel object. Can be optimoze in future
	public static double getIntersection(Vector camera, Vector alpha, Vector cord, int size){
		Vector d = Vector.negate(cord, camera);
		
		double tx1 = d.x/alpha.x;
		double tx2 = (d.x + size)/alpha.x;
		if(tx1>tx2){
			double t = tx1;
			tx1 = tx2;
			tx2 = t;
		}
		double tx = tx1;
		if(tx<0) tx = tx2;
		
		double ty1 = (d.y + size)/alpha.y;
		double ty2 = d.y/alpha.y;
		if(ty1>ty2){
			double t = ty1;
			ty1 = ty2;
			ty2 = t;
		}
		double ty = ty1;
		if(ty<0) ty = ty2;
		
		double tz1 = (d.z + size)/alpha.z;
		double tz2 = d.z/alpha.z;
		if(tz1>tz2){
			double t = tz1;
			tz1 = tz2;
			tz2 = t;
		}
		double tz = tz1;
		if(tx<0) tz = tz2;
		
		double t = -1;
		if(tx>= ty1 && tx<= ty2 && tx>= tz1 && tx<= tz2 && (tx<t || t == -1)) t = tx;
		if(ty>= tx1 && ty<= tx2 && ty>= tz1 && ty<= tz2 && (ty<t || t == -1)) t = ty;
		if(tz>= ty1 && tz<= ty2 && tz>= tx1 && tz<= tx2 && (tz<t || t == -1)) t = tz;
		return t;
	}
}