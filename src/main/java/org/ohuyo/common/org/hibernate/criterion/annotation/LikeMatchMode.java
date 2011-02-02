package org.ohuyo.common.org.hibernate.criterion.annotation;

import org.hibernate.criterion.MatchMode;

/**
 * like∆•≈‰ƒ£ Ω
 * 
 * @author rabbit
 * 
 */
public enum LikeMatchMode {
	exact(MatchMode.EXACT), start(MatchMode.START), end(MatchMode.END), anywhere(
			MatchMode.ANYWHERE);

	private MatchMode matchMode;

	private LikeMatchMode(MatchMode matchMode) {
		this.matchMode = matchMode;
	}

	public MatchMode hibernateMatchMode() {
		return matchMode;
	}

}
