//
//  BMSQ_redMemberViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/27.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_redMemberViewController.h"

#import "SVProgressHUD.h"
#import "UIImageView+AFNetworking.h"
@interface BMSQ_redMemberViewController ()

@property (nonatomic, assign)int page;

@property (nonatomic, strong)UITableView *dataTable;
@property (nonatomic, strong)NSMutableArray  *dataArray;

@end

@implementation BMSQ_redMemberViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [super setNavBackItem];
    [super setNavTitle:@"领取人数"];

    self.page = 1;
    self.dataArray = [[NSMutableArray alloc]init];
    
    self.dataTable = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.dataTable.backgroundColor = [UIColor clearColor];
    self.dataTable.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.dataTable.dataSource =self;
    self.dataTable.delegate =self;
    [self.view addSubview:self.dataTable];
    [self.dataTable addFooterWithTarget:self action:@selector(footRefresh)];
    
    [self listGrabBonus];
    
    
}
#pragma mark UITableViewDelegate && UITableViewDataSource
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.dataArray.count;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 80;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *identifier = @"Coupon_redMemberCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell ==nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 79)];
        bgView.backgroundColor = [UIColor whiteColor];
        [cell.contentView addSubview:bgView];
        
        UIImageView *headImageView = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 60, 60)];
        headImageView.backgroundColor = [UIColor redColor];
        headImageView.tag = 100;
        [cell.contentView addSubview:headImageView];
        
        UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(80, 10, APP_VIEW_WIDTH-100, 30)];
        nameLabel.backgroundColor = [UIColor clearColor];
        nameLabel.text = @"小鸡";
        nameLabel.font = [UIFont systemFontOfSize:15];
        nameLabel.tag = 101;
        [cell.contentView addSubview:nameLabel];
        
        UILabel *money = [[UILabel alloc]initWithFrame:CGRectMake(80, 40, APP_VIEW_WIDTH-100, 30)];
        money.backgroundColor = [UIColor clearColor];
        money.font = [UIFont systemFontOfSize:13];
        money.text = @"9元";
        money.tag = 102;
        [cell.contentView addSubview:money];
        
        UILabel *timeLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 10, APP_VIEW_WIDTH/2-10, 30)];
        timeLabel.backgroundColor = [UIColor clearColor];
        timeLabel.font =[UIFont systemFontOfSize:13];
        timeLabel.textAlignment = NSTextAlignmentRight;
        timeLabel.text = @"0000-00-00 00:00:00";
        timeLabel.tag = 103;
        [cell.contentView addSubview:timeLabel];
        
        
    }
    
    NSDictionary *dic = [self.dataArray objectAtIndex:indexPath.row];
    UIImageView *headView = (UIImageView *)[cell.contentView viewWithTag:100];
    UILabel *nameLabel = (UILabel *)[cell.contentView viewWithTag:101];
    UILabel *moneylabel = (UILabel *)[cell.contentView viewWithTag:102];
    UILabel *timeLabel = (UILabel *)[cell.contentView viewWithTag:103];

    NSString *urlStr =[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"avatarUrl"]];
    [headView setImageWithURL:[NSURL URLWithString:urlStr] placeholderImage:[UIImage imageNamed:@"Login_Icon"]];
    nameLabel.text = [dic objectForKey:@"nickName"];
    moneylabel.text = [NSString stringWithFormat:@"%@元",[dic objectForKey:@"value"]];
    timeLabel.text = [dic objectForKey:@"getDate"];

    return cell;
}

- (void)listGrabBonus
{
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.bonusCode forKey:@"bonusCode"];
    [params setObject:[NSNumber numberWithInt:self.page] forKey:@"page"];
    NSString* vcode = [gloabFunction getSign:@"listGrabBonus" strParams:self.bonusCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"listGrabBonus" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        weakSelf.page = weakSelf.page+1;
        if ([responseObject objectForKey:@"bonusList"]) {
            [weakSelf.dataArray addObjectsFromArray:[responseObject objectForKey:@"bonusList"]];
        }
        [weakSelf.dataTable reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
}
-(void)footRefresh{
    [self.dataTable footerEndRefreshing];
    [self listGrabBonus];
    
}
@end
