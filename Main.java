
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.RepaintManager;


import java.util.ArrayList;


class Main extends Component{
	private static int fps_time = 70;
	
	private BufferedImage buffer_screen;
	private double k_width;
	
	private double fps = 0;
	
	Camera camera = new Camera(new Vector(200,0,0), new Vector(-4,0,0));
	
	private ArrayList<Object> mass = new ArrayList<Object>();
	private PointLight l = new PointLight(new Vector(100, 100, 100));
	
	//Material lite = new Material(255,255,255);
	//Material dark = new Material(128,128,128);
	
	
	Main(int w, int h){
		k_width = (double)w/h;
		buffer_screen = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		this.setBounds(0,0,w,h);
		MainListener ml = new MainListener();
		this.addKeyListener(ml);
		this.addMouseListener(ml);
		this.addMouseMotionListener(ml);
		
		mass.add(new Cube(new Vector(0,20,0), 10));
		mass.add(new Sphere(Vector.ZERO, 10));
		mass.add(new Plane(new Vector(0,0,1), -30));;
	}
	
	public void getFps(long delt){
		fps = 1000.0/delt;
	}
	
	private void updateScreenBuffer(){
		for(int x = -this.getWidth()/2; x< this.getWidth()/2; x++){
			for(int y = -this.getHeight()/2; y< this.getHeight()/2; y++){
				getPixel(x,y);
			}
		}
	}
	private void getPixel(int x, int y){
		int a = 0;
		
		Vector view = Vector.getOne(Vector.sum(camera.alpha, Vector.multiply(new Vector(0,x/(double)this.getWidth()*k_width,-y/(double)this.getHeight()), Vector.getLength(this.camera.alpha))));
		
		view = Vector.rotateVector(view, camera.a_z, camera.a_y);
		double t = -1;
		int index = 0;
		for(int i = 0; i< this.mass.size(); i++){
			double t1 = this.mass.get(i).getMinIntersection(this.camera.cord,view);
			if(t1>0){
				if(t1< t || t == -1){
					t = t1;
					index = i;
				}
			}
		}
		if(t>0 && t< 10000){
			Vector dote = Vector.sum(this.camera.cord, Vector.multiply(view, t));
			Vector n = this.mass.get(index).getNormal(dote);
			double k = Vector.scalarMul(Vector.getOne(n), Vector.getOne(Vector.negate(this.l.cord, dote)));
			if(k<0) k = 0;
			a = Material.getUnityColor(k);
			
		}
		this.buffer_screen.setRGB(x+this.getWidth()/2, y+this.getHeight()/2, a);
	}
	/*private double checkIntersection(Vector c, Vector v){				// check intersection of vector with all objects
		double t = -1;
		for(int i = 0; i< this.mass.size(); i++){
			double t1 = this.mass.get(i).getMinIntersection(c,v);
			if(t1>0){
				if(t1< t || t == -1){
					t = t1;
				}
			}
		}
		return t;
	}*/
	
	@Override
	public void paint(Graphics g){ 										// function of paint.
		updateScreenBuffer();
		g.drawImage(buffer_screen, 0,0,null);
		g.setColor(Color.white);
		g.drawString("fps: "+ fps, 0,15);
	}
	
	
	public static void main(String[] args){								// initialize
		JFrame f = new JFrame();
		f.setSize(1280,620);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Main g = new Main(f.getWidth(), f.getHeight());
		g.setFocusable(true);
		f.add(g);
		
		f.setLayout(null);
		f.setVisible(true);
		
		RepaintManager rm = new RepaintManager();
		long delt = 0;
		while(true){//mainloop
			long d0 = System.currentTimeMillis();
			f.repaint(0,0,f.getWidth(),f.getHeight());
			g.getFps(delt);
			rm.paintDirtyRegions();
			delt = System.currentTimeMillis() - d0;
			try{
				Thread.currentThread().sleep(fps_time - delt);
				delt = fps_time;
			}catch(Exception e){}
		}
	}
	
	class MainListener implements KeyListener, MouseMotionListener, MouseListener{							// system of control
		private double x0 = 0;
		private double y0 = 0;
		
		
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode() == KeyEvent.VK_W){
				camera.cord.x -= 5;
			}else if(e.getKeyCode() == KeyEvent.VK_S){
				camera.cord.x += 5;
			}
			if(e.getKeyCode() == KeyEvent.VK_A){
				camera.cord.y -= 5;
			}else if(e.getKeyCode() == KeyEvent.VK_D){
				camera.cord.y += 5;
			}
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				camera.cord.z += 5;
			}else if(e.getKeyCode() == KeyEvent.VK_SHIFT){
				camera.cord.z -= 5;
			}
			if(e.getKeyCode() == KeyEvent.VK_1){
				camera.a_y += 0.1;
			}else if(e.getKeyCode() == KeyEvent.VK_2){
				camera.a_y -= 0.1;
			}
		}
		public void keyTyped(KeyEvent e){}
		public void keyReleased(KeyEvent e){}
		
		public void mouseExited(MouseEvent e){}
		public void mouseEntered(MouseEvent e){}
		public void mouseClicked(MouseEvent e){}
		public void mousePressed(MouseEvent e){this.x0 = e.getX(); this.y0 = e.getY();}
		public void mouseReleased(MouseEvent e){}
		
		public void mouseDragged(MouseEvent e){
			//System.out.println(this.x0 + " "+this.y0);
			camera.a_y += (e.getY()-y0)/200.0;
			camera.a_z -= (e.getX() - x0)/200.0;
			this.x0 = e.getX(); this.y0 = e.getY();
		}
		public void mouseMoved(MouseEvent e){}
	}
}

