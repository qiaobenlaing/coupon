//
//  BMSQ_CardRuleViewController.m
//  BMSQS
//
//  Created by lxm on 15/7/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_CardRuleViewController.h"
#import "UIImageView+WebCache.h"
#import "BMSQ_MemberCardChooseLevelViewController.h"

@interface BMSQ_CardRuleViewController ()

@end

@implementation BMSQ_CardRuleViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationItem.hidesBackButton = NO;
    [self.navigationItem setTitle:@"会员卡规则"];

    [self customRightBtn];
    // Do any additional setup after loading the view.
    [self createModel];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}


#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    
    if([segue.identifier isEqualToString:@"BMSQ_MemberCardChooseLevel"]){
        
        BMSQ_MemberCardChooseLevelViewController *aVC = (BMSQ_MemberCardChooseLevelViewController*)segue.destinationViewController;
        aVC.cardDataArray = self.cardDataArray;
    }
    
}



- (void)customRightBtn
{
    UIBarButtonItem *item = [[UIBarButtonItem alloc] initWithTitle:@"编辑" style:UIBarButtonItemStylePlain target:self action:@selector(rightButtonClicked:)];
    [item setTitleTextAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:16.f],NSForegroundColorAttributeName: [UIColor whiteColor],} forState:UIControlStateNormal];
    self.navigationItem.rightBarButtonItem = item;
}


#pragma mark - Action

- (void)rightButtonClicked:(id)sender
{
    if(self.cardDataArray.count>0){
        [self performSegueWithIdentifier:@"BMSQ_MemberCardChooseLevel" sender:nil];
    }
}


- (void)createModel
{
    if(!self.dataSource)
        self.dataSource = [[NSMutableArray alloc] init];
    
    if(!self.cellDataSource)
        self.cellDataSource = [[NSMutableDictionary alloc] init];
    
    if(self.cardDataArray.count>0){
        /*卡等级*/
        [self.dataSource addObject:
         @{@"cardLvl":[NSString stringWithFormat:@"%ld级",self.cardDataArray.count],
           @"Type":@"MemberTableViewCell1"}];
    }
    for(NSDictionary *dicTemp in self.cardDataArray){

        if([dicTemp objectForKey:@"cardName"]){
            /*卡名*/
            NSString *imageName ;
            if([[dicTemp objectForKey:@"cardLvl"] intValue]==1) {
                imageName = @"Member_AgCard_Full";
            }else if([[dicTemp objectForKey:@"cardLvl"] intValue]==2) {
                imageName = @"Member_AuCard_Full";
            }else if([[dicTemp objectForKey:@"cardLvl"] intValue]==3) {
                imageName = @"Member_PtCard_Full";
            }
            NSString *str4 = [NSString stringWithFormat:@"%d月",[[dicTemp objectForKey:@"pointLifeTime"] intValue]];

            [self.dataSource addObject:
             @{@"cardName":[dicTemp objectForKey:@"cardName"],
               @"imageName":imageName,
               @"content":str4,
               @"Type":@"CardTableViewCell"}];
        }
        if([dicTemp objectForKey:@"discountRequire"]){
            /*所需积分*/
            NSString *str1 = [NSString stringWithFormat:@"%d分",[[dicTemp objectForKey:@"discountRequire"] intValue]];
            
            NSString *str2 = [NSString stringWithFormat:@"%d折",[[dicTemp objectForKey:@"discount"] intValue]];

            NSString *str3 = [NSString stringWithFormat:@"积%d分",[[dicTemp objectForKey:@"pointsPerCash"] intValue]];


            [self.dataSource addObject:@{@"data":
             @[@{@"name":@"需要积分",@"content":str1},
               @{@"name":@"享受折扣",@"content":str2},
               @{@"name":@"每1元",@"content":str3}],
             @"Type":@"MemberTableViewCell2"}];
            
        }
    }
    NSLog(@"_datasource=%@",self.dataSource);
    
    [self httpRequest];

}

#pragma mark -Delegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return _dataSource.count;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    NSDictionary *dic = [_dataSource objectAtIndex:section];
    if([[dic objectForKey:@"Type"] isEqualToString:@"MemberTableViewCell1"]){
        return 1;
    }else if([[dic objectForKey:@"Type"] isEqualToString:@"MemberTableViewCell2"]){
        return 3;
    }else if([[dic objectForKey:@"Type"] isEqualToString:@"CardTableViewCell"]){
        return 1;
    }
    return 0;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    NSDictionary *dic = [_dataSource objectAtIndex:indexPath.section];
    if([[dic objectForKey:@"Type"] isEqualToString:@"MemberTableViewCell1"]){
        return 44.f;
    }else if([[dic objectForKey:@"Type"] isEqualToString:@"MemberTableViewCell2"]){
        return 44.f;
    }else if([[dic objectForKey:@"Type"] isEqualToString:@"CardTableViewCell"]){
        return 204.f;
    }
    return 0;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier = nil;
    NSDictionary *dic = [_dataSource objectAtIndex:indexPath.section];
    if([[dic objectForKey:@"Type"] isEqualToString:@"MemberTableViewCell1"]){
        cellIdentifier = @"MemberTableViewCell";
    }else if([[dic objectForKey:@"Type"] isEqualToString:@"MemberTableViewCell2"]){
        cellIdentifier = @"MemberTableViewCell";
    }else if([[dic objectForKey:@"Type"] isEqualToString:@"CardTableViewCell"]){
        cellIdentifier = @"CardTableViewCell";
    }
    UITableViewCell *setCell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (setCell == nil) {
        setCell = [[UITableViewCell alloc] initWithStyle:0 reuseIdentifier:cellIdentifier];
        
    }else{
//        for(UIView *view in setCell.contentView.subviews){
//            if(view.tag==100||view.tag==101){
//                [view removeFromSuperview];
//            }
//        }
    }
    
    if([[dic objectForKey:@"Type"] isEqualToString:@"MemberTableViewCell1"]){
        UILabel *titleLabel = (UILabel *)[setCell.contentView viewWithTag:100];
        titleLabel.text = @"会员等级数";
        
        NSString *cStr  = [[self.dataSource objectAtIndex:indexPath.section] objectForKey:@"cardLvl"];
        cStr = [cStr stringByReplacingOccurrencesOfString:@"1" withString:@"一"];
        cStr = [cStr stringByReplacingOccurrencesOfString:@"2" withString:@"二"];
        cStr = [cStr stringByReplacingOccurrencesOfString:@"3" withString:@"三"];
        cStr = [cStr stringByReplacingOccurrencesOfString:@"4" withString:@"四"];
        cStr = [cStr stringByReplacingOccurrencesOfString:@"5" withString:@"五"];

        UILabel *contentLabel = (UILabel *)[setCell.contentView viewWithTag:101];
        contentLabel.text = cStr?cStr:@"";
    }else if([[dic objectForKey:@"Type"] isEqualToString:@"MemberTableViewCell2"]){
        NSArray *arrTemp = [[self.dataSource objectAtIndex:indexPath.section] objectForKey:@"data"];
        NSDictionary *dicTemp = [arrTemp objectAtIndex:indexPath.row];
        UILabel *titleLabel = (UILabel *)[setCell.contentView viewWithTag:100];
        titleLabel.text = [dicTemp objectForKey:@"name"];
        
        NSString *cStr = [dicTemp objectForKey:@"content"];
        UILabel *contentLabel = (UILabel *)[setCell.contentView viewWithTag:101];
        contentLabel.text = cStr?cStr:@"";
    }else if([[dic objectForKey:@"Type"] isEqualToString:@"CardTableViewCell"]){
        NSDictionary *dicTemp = [self.dataSource objectAtIndex:indexPath.section];
        UIImageView *imageView = (UIImageView *)[setCell viewWithTag:102];
        imageView.image = [UIImage imageNamed:[dicTemp objectForKey:@"imageName"] ];
        NSDictionary *inputImage = [_cellDataSource objectForKey:@"shopInfo"];
        UIImageView *logoImageView = (UIImageView *)[setCell viewWithTag:103];
        [logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@/%@",APP_SERVERCE_HOME,[inputImage objectForKey:@"logoUrl"]]]];
        
        UILabel *titleLabel = (UILabel *)[setCell.contentView viewWithTag:104];
        titleLabel.text = [inputImage objectForKey:@"shopName"];
        
        UILabel *timeLabel = (UILabel *)[setCell.contentView viewWithTag:105];
        timeLabel.text = [NSString stringWithFormat:@"有效期：%d月",[[dicTemp objectForKey:@"content"] intValue]];
    }
    
    setCell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return setCell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section;
{
    return 18.f;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 9)];
    v.backgroundColor = [UIColor clearColor];
    return v;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section;
{
    return 9.f;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 9)];
    v.backgroundColor = [UIColor clearColor];
    return v;
}

#pragma mark - HTTP
- (void)httpRequest
{
    NSString* vcode = [gloabFunction getSign:@"sGetShopInfo" strParams:[gloabFunction getShopCode]];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    NSDictionary *dic = @{@"shopCode":[gloabFunction getShopCode],
                          @"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"reqtime": [gloabFunction getTimestamp]};
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"sGetShopInfo" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){
            _cellDataSource = responseObject;
            [_tableView reloadData];
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(error.localizedDescription);
    }];
}
@end
