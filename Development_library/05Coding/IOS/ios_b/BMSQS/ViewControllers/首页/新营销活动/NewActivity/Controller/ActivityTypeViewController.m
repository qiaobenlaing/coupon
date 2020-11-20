//
//  ActivityTypeViewController.m
//  BMSQS
//
//  Created by gh on 15/12/29.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "ActivityTypeViewController.h"
#import "SVProgressHUD.h"

@interface ActivityTypeViewController () <UITableViewDataSource, UITableViewDelegate>


@property (nonatomic, strong)UITableView *tableView;





@end


@implementation ActivityTypeViewController


- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"newTopActivityType" object:nil];
    
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavTitle:@"活动类型"];
    [self setNavBackItem];
    [self setViewUp];
    
}

- (void)setViewUp {
    

//    self.actTypeArray = @[@"", @"聚会", @"运动", @"户外", @"亲子", @"体验课", @"音乐"];
    
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 64) style:UITableViewStylePlain];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.backgroundColor = UICOLOR(239, 239, 244, 1);
    self.tableView.keyboardDismissMode  = UIScrollViewKeyboardDismissModeInteractive;
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:self.tableView];
    

}



#pragma mark - UITabelView Delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.actTypeArray.count;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 44;
  
    
    
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
 
    NSString *identify = @"activityTypeCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identify];
    if (cell == nil) {
        cell= [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identify];
        
        UILabel *laebl = [[UILabel  alloc] initWithFrame:CGRectMake(40, 0, APP_VIEW_WIDTH-30, cell.frame.size.height)];
        laebl.font = [UIFont systemFontOfSize:13.f];
        laebl.tag = 2001;
        [cell.contentView addSubview:laebl];
        
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 15, 15)];
        imageView.tag = 3001;
        imageView.center = CGPointMake(20, cell.frame.size.height/2);
        [cell.contentView addSubview:imageView];
        
        UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, 0.5)];
        lineView.backgroundColor = APP_CELL_LINE_COLOR;
        [cell.contentView addSubview:lineView];
        
        
    }
    UIImageView *imageView = [cell.contentView viewWithTag:3001];

    
    UILabel *laebl = [cell viewWithTag:2001];
    NSDictionary *dic = self.actTypeArray[indexPath.row];
    laebl.text = [dic objectForKey:@"name"];
    
    if ([[dic objectForKey:@"value"] intValue] == _selectCellRow) {
        imageView.image = [UIImage imageNamed:@"radio_yes"];
    }else {
        imageView.image = [UIImage imageNamed:@"radio_no"];
    }
    
    return cell;
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    [self performSelector:@selector(deselect) withObject:nil afterDelay:0.2f];
    
    NSDictionary *dic = [self.actTypeArray objectAtIndex:indexPath.row];
    self.selectCellRow = [[dic objectForKey:@"value"] intValue];
    
    [self.tableView reloadData];

    NSDictionary *notificationDic = [[NSDictionary alloc] initWithObjectsAndKeys:[dic objectForKey:@"value"], @"row",  nil];
    NSNotification *notification =[NSNotification notificationWithName:@"newTopActivityType"
                                                                 object:nil
                                                               userInfo:notificationDic];
    
    [[NSNotificationCenter defaultCenter]postNotification:notification];
    [self.navigationController popViewControllerAnimated:YES];

    
}

- (void)deselect
{
    [self.tableView deselectRowAtIndexPath:[self.tableView indexPathForSelectedRow] animated:YES];
    
}






@end
