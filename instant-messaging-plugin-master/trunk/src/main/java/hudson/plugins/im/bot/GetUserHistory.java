package hudson.plugins.im.bot;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import hudson.model.AbstractBuild;
import hudson.model.User;

public class GetUserHistory implements Collection<AbstractBuild<?, ?>> {
	
	private Collection<AbstractBuild<?, ?>> builds;
	private String username;
	
	public GetUserHistory(Collection<AbstractBuild<?, ?>> builds, String username) {
		this.builds = builds;
		this.username = username;
		applyFilter();
		
	}
	
	/**
	 * filter method
	 * It apply filter to received data (builds)
	 */
	private void applyFilter() {
		for(AbstractBuild abBuild: builds) {
			
			// testing with getCulprits method
			Set users = abBuild.getCulprits();
			if(!users.equals(User.get(username))) {
				builds.remove(abBuild);
			}
			
		}
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return builds.size();
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return builds.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return builds.contains(o);
	}

	@Override
	public Iterator<AbstractBuild<?, ?>> iterator() {
		// TODO Auto-generated method stub
		return builds.iterator();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return builds.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return builds.toArray(a);
	}

	@Override
	public boolean add(AbstractBuild<?, ?> e) {
		// TODO Auto-generated method stub
		return builds.add(e);
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return builds.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return builds.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends AbstractBuild<?, ?>> c) {
		// TODO Auto-generated method stub
		return builds.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return builds.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return builds.retainAll(c);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		builds.clear();
	}
}
