import ShoppingListTableItem from "../ShoppingListTableItem/ShoppingListTableItem";

export default function ShoppingListTable() {
  return (
    <div className="shopping-list-table__wrapper">
      <table>
        <tr>
          <th>#</th>
          <th>Name</th>
          <th>Amount</th>
          <th>Measure</th>
          <th>Delete</th>
        </tr>
        <ShoppingListTableItem
          id={232}
          name="Super long name of Item"
          amount={25.302}
        />
        <ShoppingListTableItem
          id={23}
          name="Super long name of Item"
          amount={2502}
        />
        <ShoppingListTableItem
          id={232}
          name="Super long name of Item and one more super long"
          amount={3.342}
        />
        <ShoppingListTableItem
          id={123}
          name="Super long name of Item Super long name of Item one more super long "
          amount={12}
        />
        <ShoppingListTableItem
          id={321}
          name="Super long name of Item"
          amount={2}
        />
      </table>
    </div>
  );
}
