#Not-The-Library-Of-Babel (under construction)

Do you know Borges' _Library of Babel_? 
If not then read what's his diabolically mathematical 😈 library: <br>

&minus; [in Polish](https://przekroj.pl/kultura/biblioteka-babel1-jorge-luis-borges), or <br>
&minus; [in English](https://maskofreason.files.wordpress.com/2011/02/the-library-of-babel-by-jorge-luis-borges.pdf)

Below I present the catalog of a library that for sure isn't _The Library Of Babel_ &minus; 
it's a small library of my favourite Java books. 
The catalog is available in a frame of the RESTful CRUD application, part of which is deployed on Heroku. 
The code deployed on Heroku may be found in repo at 
**deploy** branch (read only version), whereas the hole code (CRUD version) &minus; 
at **develop** branch. Following _Agile_ methodology 
and the rules of creating high quality code, 
each time I pushed the version of my application to my GitHub repository, 
I've ensured earlier that my application works. 

The following endpoints are available on Heroku so far 
(the project isn't completed yet):
<ul>
<li>
get the list of all authors that wrote a book appearing in my library:

`https://heroku-library.herokuapp.com/v1/library/authors`
</li>
<li>
get some author chosen at random:

`https://heroku-library.herokuapp.com/v1/library/authors/random`
</li>
<li>
get the author placed under (e.g.) id = 1:

`https://heroku-library.herokuapp.com/v1/library/authors/1`
</li>
</ul>

Fortunately, each time you use the catalog, you will obtain a finite _json_, in 
contrast to _The Library Of Babel_, that is _''so enormous that any
reduction of human origin is infinitesimal''..._




