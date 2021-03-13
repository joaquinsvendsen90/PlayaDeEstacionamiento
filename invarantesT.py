import fileinput
import re
import os

CURR_DIR = os.getcwd()
directorio1 = CURR_DIR + '/LogDisparos.csv'
directorio2 = CURR_DIR + '/archivo'

def refactorizador():
	manf = open(directorio1)
	f = open (directorio2,'w')
	for linea in manf:
		linea = linea.rstrip()
		x = re.findall('(T[0-9]+)', linea)
		if len(x) > 0 :
			cadena = "".join(x) + "-"
			f.write(cadena)
	f.close()

refactorizador()
manf = open(directorio2)
linea = [manf.readline(), 0]
inv1=1;inv2=1;inv3=1

print("Secuencia de Disparos realizados: \n",linea[0],"\n")	

while inv1+inv2+inv3 !=0:	
	
	linea = re.subn('T19-(.*?)T1-(.*?)T4-(.*?)(((T7-(.*?)T9-(.*?))|(T23-(.*?)T8-(.*?)T10-(.*?)T24-(.*?)))((T11-(.*?)T13-(.*?)T15-)|(T12-(.*?)T14-(.*?)T16-)))','\g<1>\g<2>\g<3>\g<7>\g<8>\g<10>\g<11>\g<12>\g<13>\g<16>\g<17>\g<19>\g<20>', linea[0].rstrip())
	inv1 = linea[1]
	linea = re.subn('T20-(.*?)T2-(.*?)T5-(.*?)(((T7-(.*?)T9-(.*?))|(T23-(.*?)T8-(.*?)T10-(.*?)T24-(.*?)))((T11-(.*?)T13-(.*?)T15-)|(T12-(.*?)T14-(.*?)T16-)))','\g<1>\g<2>\g<3>\g<7>\g<8>\g<10>\g<11>\g<12>\g<13>\g<16>\g<17>\g<19>\g<20>', linea[0].rstrip())
	inv2 = linea[1]
	linea = re.subn('T21-(.*?)T3-(.*?)T6-(.*?)(((T7-(.*?)T9-(.*?))|(T23-(.*?)T8-(.*?)T10-(.*?)T24-(.*?)))((T11-(.*?)T13-(.*?)T15-)|(T12-(.*?)T14-(.*?)T16-)))','\g<1>\g<2>\g<3>\g<7>\g<8>\g<10>\g<11>\g<12>\g<13>\g<16>\g<17>\g<19>\g<20>', linea[0].rstrip())
	inv3 = linea[1]
	
if not linea[0]:
	print("Se han respetado todas las invariantes de transición\n")
else:
	print("No se ha respetado alguna de las invariantes de transición.\n")
	print("estas son las transiciónes que quedaron: ",linea[0],"\n")

