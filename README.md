# MorpheuS
This repository provides the code to run MorpheuS, as presented in the [paper by Dorien Herremans and Elaine Chew](https://arxiv.org/abs/1812.04832)[1]. 

## Abstract
Automatic music generation systems have gained in popularity and sophistication as advances in cloud computing have enabled large-scale complex computations such as deep models and optimization algorithms on personal devices. Yet, they still face an important challenge, that of long-term structure, which is key to conveying a sense of musical coherence. We present the MorpheuS music generation system designed to tackle this problem. MorpheuS' novel framework has the ability to generate polyphonic pieces with a given tension profile and long- and short-term repeated pattern structures. A mathematical model for tonal tension quantifies the tension profile and state-of-the-art pattern detection algorithms extract repeated patterns in a template piece. An efficient optimization metaheuristic, variable neighborhood search, generates music by assigning pitches that best fit the prescribed tension profile to the template rhythm while hard constraining long-term structure through the detected patterns. This ability to generate affective music with specific tension profile and long-term structure is particularly useful in a game or film music context. Music generated by the MorpheuS system has been performed live in concerts.

![MorpheuS](https://github.com/dorienh/MorpheuS/blob/main/abstract.jpg?raw=true)

## How to run
Requirements: you need a java runtime environment, as well as an uncompressed musicxml version (.xml extension) of your input template file and a midi version. You can generate these using the MuseScore software. 

Download the `executables` folder and enter it `cd executables\`, locate the `run_morpheus.sh` script. 

Ensure that it is executable: `chmod +x morpheus_music_generation.sh`. 

Instructions will be provided when you run the script: 
`./run_morpheus.`

Running MorpheuS will do two things: extract patterns with omnisia.jar and generate a new piece with PMusicOR.jar (aka MorpheuS). 

The initial random input as well as intermediate generated pieces and final output will be written to the folder as musicxml files. There will be additional files with [tension ribbons](https://dorienherremans.com/tension) overlayed, the latter can be viewed with [InScore viewer](https://inscore.grame.fr/). 


## Known issues and improvements
MusicXML files can be a bit inconsistent. If you encounter any issues it may be due to an unexpected xml format. If your output is not satisfactory, you can try different settings for the pattern extraction, which may help. 

The MorpheuS code was written quite a while ago and could due with many improvements. It is not parallelized, prints a bit too much output, and could be speed up. I haven't worked on this in a while but anyone if free to make commits. Currently, the xml parser does take into account transposition of certain tracks/instruments e.g. saxophone. 

## References 
If you find this repo useful, please cite our original paper: 

[1] Herremans, D., & Chew, E. (2017). MorpheuS: generating structured music with constrained patterns and tension. IEEE Transactions on Affective Computing, 10(4), 510-523.

```
@article{herremans2017morpheus,
  title={MorpheuS: generating structured music with constrained patterns and tension},
  author={Herremans, Dorien and Chew, Elaine},
  journal={IEEE Transactions on Affective Computing},
  volume={10},
  number={4},
  pages={510--523},
  year={2017},
  publisher={IEEE}
}
```

Note, this repo uses David Meredith's [Omnisia](https://github.com/chromamorph/omnisia). 
