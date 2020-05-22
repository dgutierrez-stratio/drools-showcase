clean:
	bin/clean.sh

package:
	bin/package.sh $(version)

test:
	bin/test.sh
