const express = require('express')
const {ObjectId} = require('mongodb')
const {connectToDb, getDb} = require('./db')

//init app & middleware
const app = express();
app.use(express.json())

//db connection
let db;

connectToDb((err)=>{
    if(!err){
        app.listen(3000, ()=>{
            console.log('app listening on post 3000')
        })
        db = getDb()
    }
})

//routes
app.get('/books',(req,res)=>{
    if (!db) {
        return res.status(500).json({ error: 'Database not connected' });
    }

    //Current page
    const page = req.query.p || 0;
    const booksPerPage = 3;
    
    let books = [];

    db.collection('books')
        .find() //cursor toArray forEach
        .sort({author:1})
        .skip(page * booksPerPage)  //page for record  search link eg: http://localhost:3000/books/?p=0 
        .limit(booksPerPage)    ////limt the record display number
        .toArray() // Convert cursor to array (better than forEach here)
        // .forEach(book => books.puch(book))
        .then((result) => {
            res.status(200).json(result)
        })
        .catch((err)=>{
            res.status(500).json({error: 'Could not fetch the documents'})
        });
})

app.get('/books/:id',(req,res)=>{ //get data
    if (!db) {
        return res.status(500).json({ error: 'Database not connected' });
    }

    if (ObjectId.isValid(req.params.id)){
        db.collection('books')
        .findOne({ _id: new ObjectId(req.params.id)})
        // .forEach(book => books.puch(book))
        .then((result) => {
            res.status(200).json(result)
        })
        .catch((err)=>{
            res.status(500).json({error: 'Could not fetch the documents'})
        });
    }else{
        res.status(500).json({error:"Not a valid doc id"})
    }
})

app.post('/books',(req,res) => {  //Post function
    const book = req.body
    db.collection('books')
        .insertOne(book)
        .then(result =>{
            res.status(201).json(result)
        })
        .catch(err => {
            res.status(500).json({err:'Could not create a new document'})
        })
})

app.delete('/books/:id',(req,res)=>{ //Delete item
    if (ObjectId.isValid(req.params.id)){
        db.collection('books')
        .deleteOne({ _id: new ObjectId(req.params.id)})
        .then((result) => {
            res.status(200).json(result)
        })
        .catch((err)=>{
            res.status(500).json({error: 'Could not delete the documents'})
        });
    }else{
        res.status(500).json({error:"Not a valid doc id"})
    }
})

app.patch('/books/:id',(req,res)=>{ //Update
    const updates = req.body;

    //Eg {"title":"new value","rating":6}

    if (ObjectId.isValid(req.params.id)){
        db.collection('books')
        .updateOne({ _id: new ObjectId(req.params.id)},{$set: updates})
        .then((result) => {
            res.status(200).json(result)
        })
        .catch((err)=>{
            res.status(500).json({error: 'Could not update the documents'})
        });
    }else{
        res.status(500).json({error:"Not a valid doc id"})
    }
});



//Create new record sample
// {   
//     "title":"The Final Empire",
//     "author":"Brandon Sanderson",
//     "rating":9,
//     "pages":420,
//     "genres":[  
//         "fantasy",
//         "magic"
//     ],
//     "reviews":[
//     {
//     "name":"shan",
//     "body":"coud't put this book down."
//     },
//     {
//         "name":"chun-Li",
//         "body":"Love it"
//     }
//  ]
//}

//Postmain select PATCH
//Update Sample
// {
//     "page":350,
//     "rating":8
// }


//commond 
//Find data from bookstore
//db.books.find({rating:8}).explain('executionStats')
//db.books.createIndex({rating:8})
//db.books.getIndexes()
//db.books.find({rating:8}).explain('executionStats')
//drop data
//db.books.dropIndex({rating:8})


