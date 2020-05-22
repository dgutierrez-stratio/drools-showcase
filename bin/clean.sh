#!/bin/bash -e

if [[ -d target ]]; then
	sudo rm -Rf target
fi

if [[ -d env-ut ]]; then
	rm -Rf env-ut
fi