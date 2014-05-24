package ch.ethz.inf.dbproject.model.simpleDatabase.conditional;

public class Like extends BinaryConditional implements Condition {

	public Like(ConditionalSource a, ConditionalSource b) {
		super(a, b);
	}

	@Override
	protected boolean bothNotNull(String aValue, String bValue) {
		boolean ampLeft = false;
		boolean ampRight = false;
		aValue = aValue.toLowerCase();
		bValue = bValue.toLowerCase();
		int bLength = bValue.length(); 
		if (bLength > 1) {
			if ("%".equals(bValue.substring(0, 1))) {
				ampLeft = true;
				bValue = bValue.substring(1, bLength);
				bLength--;
			}
			if ("%".equals(bValue.substring(bLength-1, bLength))) {
				ampRight = true;
				bValue = bValue.substring(0, bLength-1);
				bLength--;
			}
		}
		if (!ampLeft && !ampRight) {
			return aValue.equals(bValue);
		}
		int pos = aValue.indexOf(bValue);
		if (pos == -1) {
			return false;
		}
		if (pos > 0 && !ampLeft) {
			return false;
		}
		if (!ampRight && aValue.length() - pos < bLength) {
			return false;
		}
		return true;
	}

}
