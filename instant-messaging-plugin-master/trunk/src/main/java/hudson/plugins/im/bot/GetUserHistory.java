package hudson.plugins.im.bot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import hudson.model.AbstractBuild;
import hudson.model.User;

/**
 * Returns collection of builds started by a user.
 * 
 * @author BookReaders CS 427 Group UIUC
 */
public class GetUserHistory implements Collection<AbstractBuild<?, ?>> {

	private Collection<AbstractBuild<?, ?>> filteredBuilds;
	private String username;

	/**
	 * Constructor.
	 * 
	 * @param builds
	 *            builds that filter will be applied to to find the user's
	 * @param username
	 *            username to find builds for
	 */
	public GetUserHistory(Collection<AbstractBuild<?, ?>> builds,
			String username) {
		this.username = username;
		applyFilter(builds);

	}

	private void applyFilter(Collection<AbstractBuild<?, ?>> originals) {
		filteredBuilds = new ArrayList<AbstractBuild<?, ?>>();
		for (AbstractBuild<?, ?> abBuild : originals) {
			if (abBuild.hasParticipant(User.get(username))) {
				filteredBuilds.add(abBuild);
			}
		}
	}

	@Override
	public int size() {
		return filteredBuilds.size();
	}

	@Override
	public boolean isEmpty() {
		return filteredBuilds.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return filteredBuilds.contains(o);
	}

	@Override
	public Iterator<AbstractBuild<?, ?>> iterator() {
		return filteredBuilds.iterator();
	}

	@Override
	public Object[] toArray() {
		return filteredBuilds.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return filteredBuilds.toArray(a);
	}

	@Override
	public boolean add(AbstractBuild<?, ?> e) {
		return filteredBuilds.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return filteredBuilds.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return filteredBuilds.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends AbstractBuild<?, ?>> c) {
		return filteredBuilds.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return filteredBuilds.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return filteredBuilds.retainAll(c);
	}

	@Override
	public void clear() {
		filteredBuilds.clear();
	}
}
