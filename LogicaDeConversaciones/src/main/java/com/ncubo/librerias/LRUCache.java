package com.ncubo.librerias;

import java.util.HashMap;

public class LRUCache
{
	int capacity;
	HashMap<String, Nodo> map = new HashMap<String, Nodo>();
	Nodo head = null;
	Nodo end = null;

	public LRUCache(int capacity)
	{
		this.capacity = capacity;
	}

	public byte[] obtener(String key)
	{
		if (map.containsKey(key))
		{
			Nodo valorPorRetornar = map.get(key);
			remover(valorPorRetornar);
			establecerHead(valorPorRetornar);
			return valorPorRetornar.getValor();
		}
		return null;
	}

	public void remover(Nodo n)
	{
		if (n.getPrevio() != null)
		{
			n.getPrevio().setSiguiente(n.getSiguiente());
		}
		else
		{
			head = n.getSiguiente();
		}

		if (n.getSiguiente() != null)
		{
			n.getSiguiente().setPrevio(n.getPrevio());
		}
		else
		{
			end = n.getPrevio();
		}
		
		if (map.size() >= capacity)
		{
			map.remove(end.getKey());
		}
	}

	public void establecerHead(Nodo n)
	{
		n.setSiguiente(head);
		n.setPrevio(null);
		if (head != null)
		{
			head.setPrevio(n);
		}

		head = n;
		if (end == null)
		{
			end = head;
		}
	}

	public void establecer(String key, byte[] value)
	{
		if (map.containsKey(key))
		{
			Nodo old = map.get(key);
			old.setValor(value);
			remover(old);
			establecerHead(old);
		}
		else
		{
			Nodo created = new Nodo(key, value);
			if (map.size() >= capacity)
			{
				map.remove(end.getKey());
				remover(end);
				establecerHead(created);
			}
			else
			{
				establecerHead(created);
			}

			map.put(key, created);
		}
	}

}
