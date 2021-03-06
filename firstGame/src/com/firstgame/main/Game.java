package com.firstgame.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

// Design a Game
//https://www.youtube.com/watch?v=1gir2R7G9ws
//https://www.youtube.com/watch?v=0T1U0kbu1Sk
//https://www.youtube.com/watch?v=bWHYjLJZswQ

public class Game extends Canvas implements Runnable
{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3802813213601695834L;
	
	public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;
	
	private Thread thread;
	private boolean running = false;
	
	private Handler handle;
	
	
	public Game()
	{
		
		handle = new Handler();
		
		//Where does this Method Come From?
		//It's not a method in key Adapter.
		//Is it just a Java default?
		//takes our key input class as a parameter, or a KeyAdapter instance
		this.addKeyListener(new KeyInput(handle));
		
		System.out.println("test"); 	
		
		
		new Window(WIDTH, HEIGHT, "Game to end all games", this);
		
		
		
		handle.addObject(new Player(WIDTH/2-32, HEIGHT/2-32, ID.Player));
		
		
	}

	public synchronized void start()
	{
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop()
	{
		try
		{
			thread.join();
			running = false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	public void run()
	{
		//Game loop
		//
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running)
		{
			long now = System.nanoTime();
			delta += (now -lastTime) / ns;
			lastTime = now;
			while(delta >= 1)
			{
				tick();
				delta--;
			}
			if(running)
				render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				//System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
		
	}
	
	private void tick()
	{
		handle.tick();
	}
	private void render()
	
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0,WIDTH,HEIGHT);
		
		handle.render(g);
		
		g.dispose();
		bs.show();
	}

	



	public static void main(String[] args)
	{
			new Game();
	}



		
}

