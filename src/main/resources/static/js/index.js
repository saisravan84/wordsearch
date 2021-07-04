const words = document.querySelector('#add-word');
const submitWordButton = document.querySelector('.submit-word');

class Grid {
    constructor() {
        this.wordSelectMode = false;
        this.selectedItems = [];
        this.firstSelectedItem;
        this.gridArea = null;
        this.words = [];
        this.foundWords = [];
    }

    getCellsInRange(firstLetter, currentLetter) {
        let cellsInRange = [];
        if (currentLetter.y < firstLetter.y || currentLetter.x < firstLetter.x) [currentLetter, firstLetter] = [firstLetter, currentLetter]
        if (firstLetter.x === currentLetter.x) {
            for (let i = firstLetter.y; i <= currentLetter.y; i++) {
                cellsInRange.push(this.gridArea.querySelector(`td[data-x="${currentLetter.x}"][data-y="${i}"]`));
            }
        } else if (firstLetter.y === currentLetter.y) {
            for (let i = firstLetter.x; i <= currentLetter.x; i++) {
                cellsInRange.push(this.gridArea.querySelector(`td[data-x="${i}"][data-y="${currentLetter.y}"]`));
            }
        } else if (currentLetter.y - firstLetter.y === currentLetter.x - firstLetter.x) {
            let delta = currentLetter.y - firstLetter.y;
            for (let i = 0; i <= delta; i++) {
                cellsInRange.push(this.gridArea.querySelector(`td[data-x="${firstLetter.x + i}"][data-y="${firstLetter.y + i}"]`));
            }
        }
        return cellsInRange;
    }
    renderGrid(gridSize, wordGrid) {
        const gridArea = document.querySelector(".grid-area");
        this.gridArea = gridArea;
        if (gridArea.lastChild) {
            gridArea.removeChild(gridArea.lastChild);
        }
        var tbl = document.createElement("table");
        var tblBody = document.createElement("tbody");
        let index = 0;
        for (var i = 0; i < gridSize; i++) {
            var row = document.createElement("tr");
            for (var j = 0; j < gridSize; j++) {
                var cell = document.createElement("td");
                let letter = wordGrid[index++];
                var cellText = document.createTextNode(letter);
                cell.appendChild(cellText);
                cell.setAttribute("data-x", i);
                cell.setAttribute("data-y", j);
                cell.setAttribute("data-letter", letter);
                row.appendChild(cell);
            }
            tblBody.appendChild(row);
        }
        tbl.appendChild(tblBody);
        gridArea.appendChild(tbl);

        //click handlers
        tbl.addEventListener('mousedown', (event) => {
            this.wordSelectMode = true;
            let cell = event.target;
            let x = +cell.getAttribute('data-x');
            let y = +cell.getAttribute('data-y');
            let letter = cell.getAttribute('data-letter');
            this.firstSelectedItem = {
                x, y
            }
        });
        tbl.addEventListener('mousemove', (event) => {
            if (this.wordSelectMode === true && event.target.localName === "td") {
                let cell = event.target;
                let x = +cell.getAttribute('data-x');
                let y = +cell.getAttribute('data-y');
                this.selectedItems.forEach(cell => cell.classList.remove('selected'));
                this.selectedItems = this.getCellsInRange(this.firstSelectedItem, { x, y });
                this.selectedItems.forEach(cell => cell.classList.add('selected'));
            }
        });
        tbl.addEventListener('mouseup', () => {
            this.wordSelectMode = false;
            const selectedWord = this.selectedItems.reduce((word, cell) => word += cell.getAttribute("data-letter"), "")
            const reverserSelectedWord = selectedWord.split("").reverse().join("");
            if (this.words.indexOf(selectedWord) != -1) {
                this.foundWords.push(selectedWord);
                this.selectedItems.forEach(cell => cell.classList.add('found'));
            } else if (this.words.indexOf(selectedWord.split("").reverse().join("")) != -1) {
                this.foundWords.push(reverserSelectedWord)
                this.selectedItems.forEach(cell => cell.classList.add('found'));
            }
            this.selectedItems.forEach(item => {
                item.classList.remove('selected')
            });
            this.selectedItems = []
        });
    }
}

submitWordButton.addEventListener('click', async (e) => {
    const commaSeparatedWords = document.querySelector('#add-word').value.toUpperCase().replace(/(\ )/gm, "");
    const gridSize = document.querySelector('#grid-size').value;
    const grid = new Grid();
    let result = await fetchGridInfo(gridSize, commaSeparatedWords);
    grid.words = commaSeparatedWords.split(',')
    grid.renderGrid(gridSize, result);
    document.querySelector('.word-list').textContent = commaSeparatedWords
});

const fetchGridInfo = async (gridSize, commaSeparatedWords) => {
    let response = await fetch(`./wordsearch?gridSize=${gridSize}&words=${commaSeparatedWords}`);
    let result = await response.text();
    return result.replace(/(\r\n|\n|\r)/gm, "").split(" ");
}