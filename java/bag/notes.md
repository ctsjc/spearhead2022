Features ? 

Context is something that brigs number to algorithm 


overall 


Sample is format in which we read. it also had logic for segrigation,.. basically seperates 
word with tag


Sample is used to create Event 

Event is 
    outcome : its tag 
    context : Its that Array, 
    values 


Using Feature We convert Sample to Event 

Then comes the generic part of comparable Event .... 

EventStream
    getContext -> DefaultPOSContextGenerator, ConfigurablePOSContextGenerator



Ok now Just focus how Sentence Model works.....



features = {ArrayList@2113}  size = 15
 0 = "def"
 1 = "suf=y"
 2 = "suf=ry"
 3 = "suf=ary"
 4 = "suf=Mary"
 5 = "pre=M"
 6 = "pre=Ma"
 7 = "pre=Mar"
 8 = "pre=Mary"
 9 = "w=mary"
 10 = "n1w=had"
 11 = "n2w=a"
 12 = "S=begin"
 13 = "wc=ic"
 14 = "w&c=mary,ic"
 
 
 OutcomePriorFeatureGenerator   adds def
 SuffixFeatureGenerator         adds suf=       ["y", "ry", "ary", "Mary"]
 PrefixFeatureGenerator         adds pre="
 WindowFeatureGenerator         adds w n<Index>w= p<Index>w
 WindowFeatureGenerator
 TokenClassFeatureGenerator     adds wc w&c
 PosTaggerFeatureGenerator      adds t= t2=

Used for 

 So context contains most of first part 

Event
    outcome tag
    context 


outcomeList          evt.outcome    
numTimesEventsSeen      Its always 1 
predLabels
outcomeLabels


whats the relation between feature and context 






x   last word
v   previous of x 
n   next line f


SDEventStream
    DefaultSDContextGenerator



So Thoughts !!!!!

PosTaggerFeatureGenerator   
    Used for creating Context



Question is ::: 
Who creates     

    outcomeList         Made from ComparableEvent.outcome -> Event.outcome's Index
    numTimesEventsSeen             predIndexes  -> always 1 
    predLabels          Made from predicateIndex    -> predicatesInOut.put(predicateSet[i], i); ->  ev.getContext();
    outcomeLabels       Made from omap  -> Event.getOutcome()'s last index

Then Its TwoPassDataIndexer 

    But how it knows where to put what ???
.


counter: Its map 
Key = element from context
Value = number of times the element occure in context

predicateSet
    Sorted Keys from Counter 

predicateIndex/predicateInOut
    element and its index 

counter
for (String s : ev.getContext()) {
    counter.merge(s, 1, (value, one) -> value + one);
}.


String[] predicateSet = 
                counter.entrySet().stream()
                .filter(entry -> entry.getValue() >= cutoff)
                .map(Map.Entry::getKey).sorted()
                .toArray(String[]::new);

predicatesInOut.put(predicateSet[i], i);                



FIND CONTEXT
0 = "def"
1 = "suf=O"
2 = "suf=TO"
3 = "suf=ATO"
4 = "suf=NATO"
5 = "pre=N"
6 = "pre=NA"
7 = "pre=NAT"
8 = "pre=NATO"
9 = "w=nato"
10 = "n1w=united"
11 = "n2w=states"
12 = "S=begin"
13 = "wc=ac"
14 = "w&c=nato,ac"


pmap 
pmap = {LinkedHashMap@2633}  size = 783
 "S=begin" -> {MutableContext@2744} 
 "def" -> {MutableContext@2745} 
 "n1w="" -> {MutableContext@2747} 
 "n1w=," -> {MutableContext@2749} 
 "n1w=." -> {MutableContext@2751} 
 "n1w=?" -> {MutableContext@2753} 
 "n1w=a" -> {MutableContext@2755} 
 "n1w=about" -> {MutableContext@2757} 
 "n1w=against" -> {MutableContext@2759} 
 "n1w=and" -> {MutableContext@2761} 
 "n1w=appear" -> {MutableContext@2763} 
 "n1w=as" -> {MutableContext@2765} 
 "n1w=at" -> {MutableContext@2767} 
 "n1w=but" -> {MutableContext@2769} 
 "n1w=children" -> {MutableContext@2771} 
 "n1w=cry" -> {MutableContext@2773} 
 "n1w=day" -> {MutableContext@2775} 
 "n1w=did" -> {MutableContext@2777} 
 "n1w=does" -> {MutableContext@2779} 
 "n1w=eager" -> {MutableContext@2781} 
 "n1w=everywhere" -> {MutableContext@2783} 
 "n1w=fleece" -> {MutableContext@2785} 
 "n1w=followed" -> {MutableContext@2787} 
 "n1w=go" -> {MutableContext@2789} 
 "n1w=had" -> {MutableContext@2791} 
 "n1w=he" -> {MutableContext@2793} 
 "n1w=her" -> {MutableContext@2795} 
 "n1w=his" -> {MutableContext@2797} 
 "n1w=it" -> {MutableContext@2799} 
 "n1w=know" -> {MutableContext@2801} 
 "n1w=lamb" -> {MutableContext@2803} 
 "n1w=laugh" -> {MutableContext@2805} 


 

24 Jan 2022
I studied the modeling. 
in first 









NameEntity
    outcomeLabels
        outcomeLabels = {String[9]@2559} ["organization-st...", "organization-co...", "location-start", "location-cont", "person-start", +4 more]
        0 = "organization-start"
        1 = "organization-cont"
        2 = "location-start"
        3 = "location-cont"
        4 = "person-start"
        5 = "person-cont"
        6 = "person.president-start"
        7 = "person.president-cont"
        8 = "other"

    predLabels
        0 = "S=begin"
        1 = "def"
        2 = "n1w&c=.,other"
        3 = "n1w&c=<end>;,other"
        4 = "n1w&c=assembly,ic"
        5 = "n1w&c=fogh,ic"
        6 = "n1w&c=obama,ic"
        7 = "n1w&c=parliamentary,ic"
        8 = "n1w&c=rasmussen,ic"
        9 = "n1w&c=s,sc"
        10 = "n1w&c=states,ic"
        11 = "n1w=."
        12 = "n1w=<end>;"
        13 = "n1w=assembly"
        14 = "n1w=fogh"
        15 = "n1w=obama"
        16 = "n1w=parliamentary"
        17 = "n1w=rasmussen"
        18 = "n1w=s"
        19 = "n1w=states"
        20 = "n1wc=ic"
        21 = "n1wc=other"
        22 = "n1wc=sc"
        23 = "n2w&c=.,other"
        24 = "n2w&c=assembly,ic"
        25 = "n2w&c=rasmussen,ic"
        26 = "n2w&c=s,sc"
        27 = "n2w=."
        28 = "n2w=assembly"
        29 = "n2w=rasmussen"
        30 = "n2w=s"
        31 = "n2wc=ic"
        32 = "n2wc=other"
        33 = "n2wc=sc"
        34 = "p1w&c=.,other"
        35 = "p1w&c=2010,4d"
        36 = "p1w&c=anders,ic"
        37 = "p1w&c=barack,ic"
        38 = "p1w&c=fogh,ic"
        39 = "p1w&c=nato,ac"
        40 = "p1w&c=parliamentary,ic"
        41 = "p1w&c=s,sc"
        42 = "p1w&c=u,sc"
        43 = "p1w&c=united,ic"
        44 = "p1w=."
        45 = "p1w=2010"
        46 = "p1w=anders"
        47 = "p1w=barack"


    Test//"NATO United States Barack Obama was Trump"
        0 = "wc=ac"
        1 = "w&c=nato,ac"
        2 = "n1wc=ic"
        3 = "n1w&c=united,ic"
        4 = "n2wc=ic"
        5 = "n2w&c=states,ic"
        6 = "w=nato"
        7 = "n1w=united"
        8 = "n2w=states"
        9 = "def"
        10 = "pd=null"
        11 = "w,nw=NATO,United"
        12 = "wc,nc=ac,ic"
        13 = "S=begin"
        14 = "po=other"
        15 = "pow=other,NATO"
        16 = "powf=other,ac"
        17 = "ppo=other"